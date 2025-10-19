package org.zhuyuqinlan.lemall.common.file.utils;

import io.minio.*;
import io.minio.http.Method;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * MinIO 工具类
 * 纯静态方法，封装文件上传、下载、预览、删除、生成前端直传凭证等操作
 */
public class MinIOUtils {

    /**
     * 上传：将一个输入流中的文件上传到MinIO服务器上指定的存储桶（bucket）里
     *
     * @param client      MinioClient 实例
     * @param bucket      存储桶名称
     * @param objectName  上传后对象名称
     * @param inputStream 文件流
     * @param size        文件大小
     * @param contentType 文件类型，例如 image/png
     * @throws Exception 异常
     */
    public static void uploadFile(MinioClient client, String bucket, String objectName,
                                  InputStream inputStream, long size, String contentType) throws Exception {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(inputStream, size, -1)
                        .contentType(contentType)
                        .build());
    }

    /**
     * 下载：从MinIO服务器下载指定存储桶（bucket）中的文件
     *
     * @param client     MinioClient 实例
     * @param bucket     存储桶名称
     * @param objectName 对象名称
     * @return 文件流
     * @throws Exception 异常
     */
    public static InputStream downloadFile(MinioClient client, String bucket, String objectName) throws Exception {
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build());
    }

    /**
     * 预览：生成一个预签名的URL，允许浏览器直接访问文件
     * 注意：这个返回的MinioClient创建时设置的endpoint，需自己改成publicEndpoint
     *
     * @param client     MinioClient 实例
     * @param bucket     存储桶名称
     * @param objectName 对象名称
     * @return 预签名URL
     * @throws Exception 异常
     */
    public static String getPreviewUrl(MinioClient client, String bucket, String objectName) throws Exception {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectName)
                        .build());
    }

    /**
     * 删除：从MinIO服务器上的指定存储桶中删除一个文件
     *
     * @param client     MinioClient 实例
     * @param bucket     存储桶名称
     * @param objectName 对象名称
     * @throws Exception 异常
     */
    public static void deleteFile(MinioClient client, String bucket, String objectName) throws Exception {
        client.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build());
    }

    /**
     * 生成前端直传临时凭证（POST Policy）
     * 前端可直接使用该凭证将文件上传到 MinIO，不经过后端流转
     *
     * @param client         MinioClient 实例
     * @param bucket         存储桶名称
     * @param publicEndpoint 公网访问地址
     * @param objectPrefix   文件上传目录前缀
     * @param expireSeconds  凭证有效时间（秒）
     * @return Map 包含上传地址 url 和 formData（签名参数）
     * @throws Exception 异常
     */
    public static Map<String, Object> getPostPolicy(MinioClient client, String bucket,
                                                    String publicEndpoint, String objectPrefix, int expireSeconds) throws Exception {
        PostPolicy policy = new PostPolicy(bucket, ZonedDateTime.now().plusSeconds(expireSeconds));
        policy.addStartsWithCondition("key", objectPrefix);

        Map<String, String> formData = client.getPresignedPostFormData(policy);

        Map<String, Object> result = new HashMap<>();
        result.put("url", publicEndpoint + "/" + bucket); // 上传地址
        result.put("formData", formData);                // 前端表单签名数据
        result.put("expire", System.currentTimeMillis() / 1000 + expireSeconds); // 过期时间（秒）
        result.put("dir", objectPrefix);
        return result;
    }

    /**
     * 获取文件信息：大小、MIME 类型等
     *
     * @param client     MinioClient 实例
     * @param bucket     存储桶名称
     * @param objectName 对象名称
     * @return Map 包含文件信息，例如 size 和 contentType
     * @throws Exception 异常
     */
    public static Map<String, Object> getFileInfo(MinioClient client, String bucket, String objectName) throws Exception {
        StatObjectResponse stat = client.statObject(
                StatObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );

        Map<String, Object> info = new HashMap<>();
        info.put("objectName", objectName);
        info.put("size", stat.size());              // 文件大小（字节）
        info.put("contentType", stat.contentType());// MIME 类型
        info.put("lastModified", stat.lastModified());// 最后修改时间
        return info;
    }
}
