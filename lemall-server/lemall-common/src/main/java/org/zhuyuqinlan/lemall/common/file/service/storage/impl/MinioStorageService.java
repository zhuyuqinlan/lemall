package org.zhuyuqinlan.lemall.common.file.service.storage.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.bean.CustomMinioClient;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.utils.MinioUtil;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;

/**
 * MinIO 文件存储服务（支持公共桶 + 私有桶 + 内网/公网访问）
 */
@Slf4j
@Service
public class MinioStorageService implements CloudFileStorageService {

    @Value("${minio.endpoint}")
    private String endpoint; // 内网访问地址

    @Value("${minio.publicEndpoint}")
    private String publicEndpoint; // 公网访问地址

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName.public}")
    private String bucketPublic;

    @Value("${minio.bucketName.private}")
    private String bucketPrivate;

    private final FsFileStorageMapper fileStorageMapper;

    // 用枚举管理桶类型
    private enum BucketType { PUBLIC, PRIVATE }

    private final EnumMap<BucketType, CustomMinioClient> clientInMap = new EnumMap<>(BucketType.class);
    private final EnumMap<BucketType, CustomMinioClient> clientNetMap = new EnumMap<>(BucketType.class);

    public MinioStorageService(FsFileStorageMapper fileStorageMapper) {
        this.fileStorageMapper = fileStorageMapper;
    }

    @PostConstruct
    public void init() {
        // 初始化内网和公网客户端
        clientInMap.put(BucketType.PUBLIC, new CustomMinioClient(
                MinioAsyncClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build()
        ));
        clientInMap.put(BucketType.PRIVATE, new CustomMinioClient(
                MinioAsyncClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build()
        ));
        clientNetMap.put(BucketType.PUBLIC, new CustomMinioClient(
                MinioAsyncClient.builder().endpoint(publicEndpoint).credentials(accessKey, secretKey).build()
        ));
        clientNetMap.put(BucketType.PRIVATE, new CustomMinioClient(
                MinioAsyncClient.builder().endpoint(publicEndpoint).credentials(accessKey, secretKey).build()
        ));

        // 异步创建桶，减少启动阻塞
        CompletableFuture.runAsync(() -> ensureBucket(clientInMap.get(BucketType.PUBLIC), bucketPublic, true));
        CompletableFuture.runAsync(() -> ensureBucket(clientNetMap.get(BucketType.PUBLIC), bucketPublic, true));
        CompletableFuture.runAsync(() -> ensureBucket(clientInMap.get(BucketType.PRIVATE), bucketPrivate, false));
        CompletableFuture.runAsync(() -> ensureBucket(clientNetMap.get(BucketType.PRIVATE), bucketPrivate, false));
    }

    /**
     * 确保桶存在，并设置公共读策略
     */
    private void ensureBucket(CustomMinioClient client, String bucket, boolean publicRead) {
        try {
            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()).get();
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build()).get();
                log.info("已创建桶：{}", bucket);
            }
            if (publicRead) {
                // 公共读策略 JSON 可考虑配置化
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
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build()).get();
                log.info("已为桶 [{}] 设置公共读策略", bucket);
            }
        } catch (Exception e) {
            log.error("MinIO 桶初始化失败: {}", e.getMessage(), e);
            throw new RuntimeException("MinIO 桶初始化失败", e);
        }
    }

    /**
     * 获取对应的客户端
     * @param isPublic 是否公共桶
     * @param usePublicNet 是否使用公网客户端
     */
    private CustomMinioClient getClient(boolean isPublic, boolean usePublicNet) {
        BucketType type = isPublic ? BucketType.PUBLIC : BucketType.PRIVATE;
        return usePublicNet ? clientNetMap.get(type) : clientInMap.get(type);
    }

    /**
     * 保存文件记录到数据库
     */
    private FileInfoDTO saveFileRecord(String bucket, String fileKey, long size,
                                       String contentType, String md5, boolean isPublic) {
        FsFileStorage fs = new FsFileStorage();
        fs.setBucket(bucket);
        fs.setFileKey(fileKey);
        fs.setStorageType(FileStorageConstant.MINIO_TYPE);
        fs.setSize(size);
        fs.setContentType(contentType);
        fs.setMd5(md5);
        fs.setUri(bucket + "/" + fileKey);
        fileStorageMapper.insert(fs);

        FileInfoDTO dto = new FileInfoDTO();
        BeanUtils.copyProperties(fs, dto);
        dto.setUrl(getFileUrl(fileKey, isPublic));
        return dto;
    }

    @Override
    @Transactional
    public FileInfoDTO uploadFile(String fileKey, InputStream inputStream, long size,
                                  String contentType, String md5, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, false);
        try {
            // 上传文件
            MinioUtil.uploadFile(client, bucket, fileKey, inputStream, size, contentType);
            // 入库并返回 DTO
            return saveFileRecord(bucket, fileKey, size, contentType, md5, isPublic);
        } catch (Exception e) {
            log.error("文件上传失败: {}", fileKey, e);
            // 上传失败尝试删除残留文件
            try { MinioUtil.deleteFile(client, bucket, fileKey); } catch (Exception ignored) {}
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(String fileKey, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, false);
        try {
            MinioUtil.deleteFile(client, bucket, fileKey);
            fileStorageMapper.delete(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getFileKey, fileKey));
            log.info("已删除文件: {}", fileKey);
        } catch (Exception e) {
            log.error("删除文件失败: {}", fileKey, e);
            throw new RuntimeException("删除文件失败", e);
        }
    }

    @Override
    public InputStream downloadFile(String fileKey, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, false);
        try {
            return MinioUtil.getObject(client, bucket, fileKey, null, null);
        } catch (Exception e) {
            log.error("下载文件失败: {}", fileKey, e);
            throw new RuntimeException("下载文件失败", e);
        }
    }

    @Override
    public String getFileUrl(String fileKey, boolean isPublic) {
        if (isPublic) {
            return publicEndpoint + "/" + bucketPublic + "/" + fileKey;
        } else {
            try {
                return MinioUtil.getPreviewUrl(getClient(false, true), bucketPrivate, fileKey);
            } catch (Exception e) {
                log.error("生成私有文件预签名 URL 失败: {}", fileKey, e);
                throw new RuntimeException("生成文件访问地址失败", e);
            }
        }
    }

    @Override
    @SneakyThrows
    public MultipartUploadInfo getPostPolicy(String fileKey, int expireSeconds, String uploadId, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, true);
        return MinioUtil.getPostPolicy(client, bucket, uploadId, fileKey, expireSeconds);
    }

    @Override
    @Transactional
    public FileInfoDTO complete(String fileKey, String contentType, String md5, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, false);
        try {
            StatObjectResponse stat = MinioUtil.stateObject(client, bucket, fileKey);
            long size = stat.size();
            String minioContentType = stat.contentType();
            String minioMd5 = stat.etag();

            // 多分片时 ETag 可能不同，实际可用 md5 参数或自定义 hash 校验
            if (!contentType.equals(minioContentType) || !md5.equals(minioMd5)) {
                throw new RuntimeException("文件上传过程中可能损坏或MIME类型不一致");
            }

            return saveFileRecord(bucket, fileKey, size, minioContentType, minioMd5, isPublic);
        } catch (Exception e) {
            log.error("完成文件上传失败: {}", fileKey, e);
            throw new RuntimeException("完成文件上传失败", e);
        }
    }

    @Override
    @SneakyThrows
    public MultipartUploadInfo getMultipartUploadInfo(String fileKey, long partSize, long fileSize,
                                                      int expireSeconds, String uploadId, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, true);
        return MinioUtil.initMultipartUpload(client, bucket, fileKey, partSize, fileSize, expireSeconds, uploadId);
    }

    @Override
    @Transactional
    public FileInfoDTO mergeMultipartUpload(String uploadId, String fileKey, String md5,
                                            Long size, String contentType, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = getClient(isPublic, true);
        try {
            MinioUtil.mergeMultipartUpload(client, bucket, fileKey, uploadId);
            StatObjectResponse stat = MinioUtil.stateObject(client, bucket, fileKey);

            String minioContentType = stat.contentType();
            String minioMd5 = stat.etag();

            if (!contentType.equals(minioContentType) || !md5.equals(minioMd5)) {
                throw new RuntimeException("文件上传过程中可能损坏或MIME类型不一致");
            }

            return saveFileRecord(bucket, fileKey, size, contentType, md5, isPublic);
        } catch (Exception e) {
            log.error("合并分片上传失败: {}", fileKey, e);
            throw new RuntimeException("合并分片上传失败", e);
        }
    }
}
