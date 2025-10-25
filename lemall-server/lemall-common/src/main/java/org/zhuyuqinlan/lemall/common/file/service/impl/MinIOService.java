package org.zhuyuqinlan.lemall.common.file.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.service.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.utils.MinIOUtils;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * MinIO Service
 * 对外提供上传、下载、删除、前端直传凭证等业务接口
 * 管理 MinioClient 和配置
 */
@Slf4j
@Service
public class MinIOService implements CloudFileStorageService {

    @Value("${minio.endpoint}")
    private String endpoint; // 内网访问地址

    @Value("${minio.publicEndpoint}")
    private String publicEndpoint; // 公网访问地址

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucket;

    private MinioClient minioClient;

    private final FsFileStorageMapper fileStorageMapper;

    public MinIOService(FsFileStorageMapper fileStorageMapper) {
        this.fileStorageMapper = fileStorageMapper;
    }

    /**
     * 初始化 MinioClient 并创建存储桶
     */
    @PostConstruct
    public void init() throws Exception {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        boolean found = minioClient.bucketExists(
                io.minio.BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucket).build());
        }

        // 设置桶为公共读（允许匿名 GET）
        String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": {"AWS": ["*"]},
                      "Action": ["s3:GetObject"],
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }
                """.formatted(bucket);

        minioClient.setBucketPolicy(
                io.minio.SetBucketPolicyArgs.builder()
                        .bucket(bucket)
                        .config(policy)
                        .build());
    }

    /**
     * 上传文件
     */
    @Override
    public Map<String, String> uploadFile(String objectName, InputStream inputStream, long size, String contentType) {
        try {
            MinIOUtils.uploadFile(minioClient, bucket, objectName, inputStream, size, contentType);
            FsFileStorage fsFileStorage = new FsFileStorage();
            fsFileStorage.setBucket(bucket);
            fsFileStorage.setOriginalName(objectName);
            fsFileStorage.setStorageType(FileStorageConstant.MINIO_TYPE);
            fsFileStorage.setSize(size);
            fsFileStorage.setContentType(contentType);
            fsFileStorage.setUri(bucket + "/" + objectName);
            fileStorageMapper.insert(fsFileStorage);
            Map<String,String> map = new HashMap<>();
            map.put("id",fsFileStorage.getId().toString());
            map.put("url",getFileUrl(objectName));
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件
     */
    @Override
    public InputStream downloadFile(String objectName) {
        try {
            return MinIOUtils.downloadFile(minioClient, bucket, objectName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        return publicEndpoint + "/" + bucket + "/" + objectName;
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteFile(String objectName) {
        try {
            MinIOUtils.deleteFile(minioClient, bucket, objectName);
            fileStorageMapper.delete(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getOriginalName, objectName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成前端直传临时凭证（POST Policy）
     */
    @Override
    public Map<String, Object> getPostPolicy(String objectPrefix, int expireSeconds) {
        try {
            return MinIOUtils.getPostPolicy(minioClient, bucket, publicEndpoint, objectPrefix, expireSeconds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> saveFileRecord(String originalFileName) {
        try {
            // 去minio读去文件信息
            Map<String, Object> info = MinIOUtils.getFileInfo(minioClient, bucket, originalFileName);
            long size = Long.parseLong(info.get("size").toString());
            String contentType = info.get("contentType").toString();
            // 插入信息
            FsFileStorage fsFileStorage = new FsFileStorage();
            fsFileStorage.setBucket(bucket);
            fsFileStorage.setOriginalName(originalFileName);
            fsFileStorage.setStorageType(FileStorageConstant.MINIO_TYPE);
            fsFileStorage.setSize(size);
            fsFileStorage.setContentType(contentType);
            fsFileStorage.setUri(bucket + "/" + originalFileName);
            fileStorageMapper.insert(fsFileStorage);
            Map<String,String> map = new HashMap<>();
            map.put("id",fsFileStorage.getId().toString());
            map.put("url",getFileUrl(originalFileName));
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

