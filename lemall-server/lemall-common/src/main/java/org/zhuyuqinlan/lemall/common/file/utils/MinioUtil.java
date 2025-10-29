package org.zhuyuqinlan.lemall.common.file.utils;

import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Part;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.common.file.bean.CustomMinioClient;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * MinIO 静态工具类
 * 作用：封装 MinIO 常用操作，提供静态方法，无需实例化。
 */
public class MinioUtil {

    private MinioUtil() {
        // 禁止实例化
    }

    /**
     * 获取分片上传的分片号列表
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param uploadId          分片上传 ID
     * @return List<Integer>    所有已上传分片的编号
     */
    public static List<Integer> getListParts(CustomMinioClient customMinioClient, String bucket, String fileKey, String uploadId) throws Exception {
        List<Part> parts = getParts(customMinioClient, bucket, fileKey, uploadId);
        return parts.stream().map(Part::partNumber).toList();
    }

    /**
     * 上传文件到 MinIO
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param inputStream       文件输入流
     * @param size              文件大小
     * @param contentType       文件类型（MIME）
     */
    public static void uploadFile(CustomMinioClient customMinioClient, String bucket, String fileKey,
                                  InputStream inputStream, long size, String contentType) throws Exception {
        customMinioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .stream(inputStream, size, -1) // -1 表示未知大小分块
                        .contentType(contentType)
                        .build()
        );
    }

    /**
     * 获取对象文件流，可指定偏移量和长度
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param offset            偏移量（从哪个字节开始）
     * @param contentLength     读取长度（字节数）
     * @return InputStream      文件输入流
     */
    public static InputStream getObject(CustomMinioClient customMinioClient, String bucket, String fileKey, Long offset, Long contentLength) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return customMinioClient.getObject(GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .offset(offset)
                        .length(contentLength)
                        .build())
                .get();
    }


    /**
     * 删除文件
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     */
    public static void deleteFile(CustomMinioClient customMinioClient, String bucket, String fileKey) throws Exception {
        customMinioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .build()
        );
    }

    /**
     * 获取前端上传所需的 POST 策略（表单数据 + endpoint）
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param uploadId          上传id
     * @param fileKey           文件对象名
     * @param expireSeconds     策略过期时间（秒）
     * @return PostPolicyDTO    封装了上传 URL、表单数据和过期时间
     */
    public static MultipartUploadInfo getPostPolicy(CustomMinioClient customMinioClient,
                                                    String bucket,
                                                    String uploadId,
                                                    String fileKey,
                                                    int expireSeconds) throws Exception {
        // 计算总分片数
        MultipartUploadInfo multipartUploadInfo = new MultipartUploadInfo();
        multipartUploadInfo.setPartCount(1);

        // 如果没有 uploadId，则初始化分片上传
        if (!StringUtils.hasText(uploadId)) {
            uploadId = customMinioClient.initMultiPartUpload(bucket, null, fileKey, null, null);
        }

        // 生成每个分片的预签名 URL
        List<Map<String, Object>> partsList = new ArrayList<>();
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);

        String presignedUrl = customMinioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucket)
                        .object(fileKey)
                        .expiry(expireSeconds)
                        .extraQueryParams(reqParams) // 上传分片需要携带 uploadId
                        .build()
        );

        Map<String, Object> partInfo = new HashMap<>();
        partInfo.put("partNumber", 1);
        partInfo.put("url", presignedUrl);
        partsList.add(partInfo);

        multipartUploadInfo.setParts(partsList);
        return multipartUploadInfo;
    }

    // 默认6小时有效期
    public static String getPreviewUrl(CustomMinioClient customMinioClient, String bucket, String fileKey) throws Exception {
        return getPreviewUrl(customMinioClient, bucket, fileKey, 6 * 3600);
    }

    /**
     * 获取临时访问url
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param expireSecs        过期时间（秒）
     */
    public static String getPreviewUrl(CustomMinioClient customMinioClient, String bucket, String fileKey, int expireSecs) throws Exception {
        return customMinioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(fileKey)
                        .expiry(expireSecs)
                        .build()
        );
    }

    /**
     * 初始化分片上传信息
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param partSize          每个分片大小
     * @param fileSize          文件总大小
     * @param expireSeconds     分片上传 URL 过期时间
     * @param uploadId          如果是断点续传，传已有 uploadId
     * @return MultipartUploadInfo 分片上传信息（每片 URL + 分片号）
     */
    public static MultipartUploadInfo initMultipartUpload(CustomMinioClient customMinioClient,
                                                          String bucket,
                                                          String fileKey,
                                                          long partSize,
                                                          long fileSize,
                                                          int expireSeconds,
                                                          String uploadId) throws Exception {
        // 计算总分片数
        int partCount = (int) ((fileSize + partSize - 1) / partSize);
        MultipartUploadInfo multipartUploadInfo = new MultipartUploadInfo();
        multipartUploadInfo.setPartCount(partCount);

        // 如果没有 uploadId，则初始化分片上传
        if (!StringUtils.hasText(uploadId)) {
            uploadId = customMinioClient.initMultiPartUpload(bucket, null, fileKey, null, null);
        }

        // 生成每个分片的预签名 URL
        List<Map<String, Object>> partsList = new ArrayList<>();
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);

        for (int i = 1; i <= partCount; i++) {
            String presignedUrl = customMinioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(fileKey)
                            .expiry(expireSeconds)
                            .extraQueryParams(reqParams) // 上传分片需要携带 uploadId
                            .build()
            );

            Map<String, Object> partInfo = new HashMap<>();
            partInfo.put("partNumber", i);
            partInfo.put("url", presignedUrl);
            partsList.add(partInfo);
        }

        multipartUploadInfo.setParts(partsList);
        return multipartUploadInfo;
    }

    /**
     * 合并已上传的分片
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param uploadId          分片上传 ID
     */
    public static void mergeMultipartUpload(CustomMinioClient customMinioClient, String bucket, String fileKey, String uploadId) throws Exception {
        // 获取所有分片
        List<Part> partsList = getParts(customMinioClient, bucket, fileKey, uploadId);
        Part[] parts = new Part[partsList.size()];

        // 构造 Part 对象数组
        int partNumber = 1;
        for (Part part : partsList) {
            parts[partNumber - 1] = new Part(partNumber, part.etag());
            partNumber++;
        }

        // 合并分片
        customMinioClient.mergeMultipartUpload(bucket, null, fileKey, uploadId, parts, null, null);
    }

    /**
     * 获取对象信息（元数据）
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @return StatObjectResponse 文件信息
     */
    public static StatObjectResponse stateObject(CustomMinioClient customMinioClient, String bucket, String fileKey) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return customMinioClient.statObject(StatObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileKey)
                        .build())
                .get();
    }

    // TODO 清理分片

    /**
     * 获取分片列表（私有方法）
     *
     * @param customMinioClient 自定义 MinIO 客户端
     * @param bucket            存储桶名称
     * @param fileKey           文件对象名
     * @param uploadId          分片上传 ID
     * @return List<Part>      分片对象列表
     */
    private static List<Part> getParts(CustomMinioClient customMinioClient, String bucket, String fileKey, String uploadId) throws Exception {
        int partNumberMarker = 0;
        boolean isTruncated = true;
        List<Part> parts = new ArrayList<>();

        while (isTruncated) {
            // 分页获取分片信息
            ListPartsResponse partResult = customMinioClient.listMultipart(bucket, null, fileKey, 1000, partNumberMarker, uploadId, null, null);
            parts.addAll(partResult.result().partList());

            // 检查是否还有更多分片
            isTruncated = partResult.result().isTruncated();
            if (isTruncated) {
                // 更新 partNumberMarker 获取下一页
                partNumberMarker = partResult.result().nextPartNumberMarker();
            }
        }
        return parts;
    }
}
