package org.zhuyuqinlan.lemall.common.file.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.zhuyuqinlan.lemall.common.file.dto.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.dto.PostPolicyDTO;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * MinIo工具类
 */
public class MinIOUtils {

    /* ----------------------- 基础操作 ----------------------- */

    public static void uploadFile(MinioClient client, String bucket, String fileKey,
                                  InputStream inputStream, long size, String contentType) throws Exception {
        ensureBucketExists(client, bucket);
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .stream(inputStream, size, -1)
                        .contentType(contentType)
                        .build()
        );
    }

    public static InputStream downloadFile(MinioClient client, String bucket, String fileKey) throws Exception {
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .build()
        );
    }

    // 默认6小时有效期
    public static String getPreviewUrl(MinioClient client, String bucket, String fileKey) throws Exception {
        return getPreviewUrl(client, bucket, fileKey, 6 * 3600);
    }

    public static String getPreviewUrl(MinioClient client, String bucket, String fileKey, int expireSecs) throws Exception {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(fileKey)
                        .expiry(expireSecs)
                        .build()
        );
    }

    public static void deleteFile(MinioClient client, String bucket, String fileKey) throws Exception {
        client.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .build()
        );
    }

    public static void deleteFiles(MinioClient client, String bucket, List<String> fileKeys) throws Exception {
        List<DeleteObject> objects = new ArrayList<>();
        for (String name : fileKeys) objects.add(new DeleteObject(name));

        Iterable<Result<DeleteError>> results = client.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucket)
                        .objects(objects)
                        .build()
        );
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            System.err.printf("删除失败：%s (%s)%n", error.objectName(), error.message());
        }
    }

    public static boolean exists(MinioClient client, String bucket, String fileKey) {
        try {
            client.statObject(StatObjectArgs.builder().bucket(bucket).object(fileKey).build());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static Map<String, Object> getFileInfo(MinioClient client, String bucket, String fileKey) throws Exception {
        StatObjectResponse stat = client.statObject(
                StatObjectArgs.builder().bucket(bucket).object(fileKey).build()
        );
        Map<String, Object> info = new HashMap<>();
        info.put("fileKey", fileKey);
        info.put("size", stat.size());
        info.put("contentType", stat.contentType());
        info.put("lastModified", stat.lastModified());
        return info;
    }

    public static void copyFile(MinioClient client,
                                String sourceBucket, String sourceFileKey,
                                String targetBucket, String targetFileKey) throws Exception {
        ensureBucketExists(client, targetBucket);
        client.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder()
                                .bucket(sourceBucket)
                                .object(sourceFileKey)
                                .build())
                        .bucket(targetBucket)
                        .object(targetFileKey)
                        .build()
        );
    }

    public static void moveFile(MinioClient client,
                                String sourceBucket, String sourceFileKey,
                                String targetBucket, String targetFileKey) throws Exception {
        copyFile(client, sourceBucket, sourceFileKey, targetBucket, targetFileKey);
        deleteFile(client, sourceBucket, sourceFileKey);
    }

    public static List<String> listFiles(MinioClient client, String bucket, String prefix, boolean recursive) throws Exception {
        List<String> result = new ArrayList<>();
        Iterable<Result<Item>> items = client.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build()
        );
        for (Result<Item> item : items) result.add(item.get().objectName());
        return result;
    }

    /* ----------------------- 前端直传 ----------------------- */

    /**
     * 生成前端直传凭证（POST Policy）
     */
    public static PostPolicyDTO getPostPolicy(MinioClient client,
                                              String bucket,
                                              String fileKey,
                                              int expireSeconds) throws Exception {
        // 构建策略
        PostPolicy policy = new PostPolicy(bucket, ZonedDateTime.now().plusSeconds(expireSeconds));
        if (fileKey.endsWith("/")) {
            policy.addStartsWithCondition("key", fileKey);
        } else {
            policy.addEqualsCondition("key", fileKey);
        }

        // 获取表单数据
        Map<String, String> formData = client.getPresignedPostFormData(policy);

        // 生成 endpoint
        String endpoint = client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucket)
                        .object(fileKey)
                        .build()
        );

        // 去掉 path，得到纯 endpoint
        endpoint = endpoint.replaceAll("/" + bucket + "/" + fileKey + ".*$", "");

        // 返回 DTO
        return new PostPolicyDTO(
                endpoint + "/" + bucket,
                formData,
                System.currentTimeMillis() / 1000 + expireSeconds,
                fileKey
        );
    }

    /**
     * 生成大文件分片上传凭证
     *
     * @param client        MinioClient 实例
     * @param bucket        桶名
     * @param fileKey       文件最终存储 key
     * @param partSize      每片大小（字节）
     * @param fileSize      文件总大小（字节）
     * @param expireSeconds 预签名有效期（秒）
     * @return MultipartUploadInfo 包含每片预签名 URL 和临时分片 key
     */
    public static MultipartUploadInfo initMultipartUpload(MinioClient client,
                                                          String bucket,
                                                          String fileKey,
                                                          long partSize,
                                                          long fileSize,
                                                          int expireSeconds) throws Exception {
        // 确保桶存在
        ensureBucketExists(client, bucket);

        // 计算总分片数
        int partCount = (int) ((fileSize + partSize - 1) / partSize);

        List<Map<String, Object>> partsList = new ArrayList<>();

        // 生成每片预签名 URL，上传到临时分片对象
        for (int i = 1; i <= partCount; i++) {
            String partObjectKey = fileKey + "_part" + i; // 临时分片 key
            String presignedUrl = client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(partObjectKey)
                            .expiry(expireSeconds)
                            .build()
            );

            Map<String, Object> partInfo = new HashMap<>();
            partInfo.put("partNumber", i);
            partInfo.put("url", presignedUrl);
            partInfo.put("fileKey", partObjectKey); // 临时分片路径
            partsList.add(partInfo);
        }

        // 返回分片信息
        return new MultipartUploadInfo(partsList, partCount);
    }

    /** 合并分片文件 **/
    public void mergeParts(MinioClient client, String bucket, List<String> partObjects, String fileKey) throws Exception {
        // 临时文件存储
        File tmpFile = File.createTempFile("merge_", ".tmp");

        try (OutputStream out = new FileOutputStream(tmpFile)) {
            for (String part : partObjects) {
                try (InputStream in = client.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(part)
                                .build()
                )) {
                    in.transferTo(out); // 按顺序写入
                }
            }
        }

        // 上传合并后的完整文件
        try (InputStream in = new FileInputStream(tmpFile)) {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileKey)
                            .stream(in, tmpFile.length(), -1)
                            .build()
            );
        }

        // 删除临时分片
        for (String part : partObjects) {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(part)
                    .build()
            );
        }

        if (tmpFile.delete()) {
            System.err.println("临时文件删除失败");
        }
    }



    /* ----------------------- 辅助工具 ----------------------- */

    public static void ensureBucketExists(MinioClient client, String bucket) throws Exception {
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }
}
