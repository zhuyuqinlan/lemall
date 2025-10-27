package org.zhuyuqinlan.lemall.common.file.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 封装上传、下载、预览、删除、批量删除、复制、文件信息、前端直传等常用操作。
 * 所有方法为静态方法，方便直接调用。
 */
public class MinIOUtils {

    /* ----------------------- 基础操作 ----------------------- */

    public static void uploadFile(MinioClient client, String bucket, String objectName,
                                  InputStream inputStream, long size, String contentType) throws Exception {
        ensureBucketExists(client, bucket);
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(inputStream, size, -1)
                        .contentType(contentType)
                        .build()
        );
    }

    public static InputStream downloadFile(MinioClient client, String bucket, String objectName) throws Exception {
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    public static String getPreviewUrl(MinioClient client, String bucket, String objectName) throws Exception {
        return getPreviewUrl(client, bucket, objectName, 7 * 24 * 3600);
    }

    public static String getPreviewUrl(MinioClient client, String bucket, String objectName, int expireSecs) throws Exception {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectName)
                        .expiry(expireSecs)
                        .build()
        );
    }

    public static void deleteFile(MinioClient client, String bucket, String objectName) throws Exception {
        client.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    public static void deleteFiles(MinioClient client, String bucket, List<String> objectNames) throws Exception {
        List<DeleteObject> objects = new ArrayList<>();
        for (String name : objectNames) objects.add(new DeleteObject(name));

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

    public static boolean exists(MinioClient client, String bucket, String objectName) {
        try {
            client.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static Map<String, Object> getFileInfo(MinioClient client, String bucket, String objectName) throws Exception {
        StatObjectResponse stat = client.statObject(
                StatObjectArgs.builder().bucket(bucket).object(objectName).build()
        );
        Map<String, Object> info = new HashMap<>();
        info.put("objectName", objectName);
        info.put("size", stat.size());
        info.put("contentType", stat.contentType());
        info.put("lastModified", stat.lastModified());
        return info;
    }

    public static void copyFile(MinioClient client,
                                String sourceBucket, String sourceObject,
                                String targetBucket, String targetObject) throws Exception {
        ensureBucketExists(client, targetBucket);
        client.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder()
                                .bucket(sourceBucket)
                                .object(sourceObject)
                                .build())
                        .bucket(targetBucket)
                        .object(targetObject)
                        .build()
        );
    }

    public static void moveFile(MinioClient client,
                                String sourceBucket, String sourceObject,
                                String targetBucket, String targetObject) throws Exception {
        copyFile(client, sourceBucket, sourceObject, targetBucket, targetObject);
        deleteFile(client, sourceBucket, sourceObject);
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
    public static Map<String, Object> getPostPolicy(MinioClient client,
                                                    String bucket,
                                                    String objectName,
                                                    int expireSeconds) throws Exception {
        // 构建策略
        PostPolicy policy = new PostPolicy(bucket, ZonedDateTime.now().plusSeconds(expireSeconds));
        if (objectName.endsWith("/")) {
            policy.addStartsWithCondition("key", objectName);
        } else {
            policy.addEqualsCondition("key", objectName);
        }

        Map<String, String> formData = client.getPresignedPostFormData(policy);

        // 从 client 生成 endpoint
        String endpoint = client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
        // 去掉 path，得到纯 endpoint
        endpoint = endpoint.replaceAll("/" + bucket + "/" + objectName + ".*$", "");

        Map<String, Object> result = new HashMap<>();
        result.put("url", endpoint + "/" + bucket);
        result.put("formData", formData);
        result.put("expire", System.currentTimeMillis() / 1000 + expireSeconds);
        result.put("objectName", objectName);
        return result;
    }


    /* ----------------------- 辅助工具 ----------------------- */

    public static void ensureBucketExists(MinioClient client, String bucket) throws Exception {
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public static String buildObjectPath(String fileName) {
        return String.format("%tY%<tm%<td/%s", new Date(), fileName);
    }
}
