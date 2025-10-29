package org.zhuyuqinlan.lemall.common.file.service.storage.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.SetBucketPolicyArgs;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.bean.CustomMinioClient;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.utils.MinioUtil;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;

import java.io.InputStream;

/**
 * MinIO Service（支持公共桶 + 私有桶 + 内网/公网访问）
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

    private CustomMinioClient minioClientPublicIn;   // 公共桶内网客户端
    private CustomMinioClient minioClientPrivateIn;  // 私有桶内网客户端
    private CustomMinioClient minioClientPublicNet;  // 公共桶公网客户端
    private CustomMinioClient minioClientPrivateNet; // 私有桶公网客户端（生成公网预签名 URL）

    private final FsFileStorageMapper fileStorageMapper;

    public MinioStorageService(FsFileStorageMapper fileStorageMapper) {
        this.fileStorageMapper = fileStorageMapper;
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        // 初始化 4 个 MinioAsyncClient（异步客户端）
        minioClientPublicIn = new CustomMinioClient(MinioAsyncClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build());

        minioClientPublicNet = new CustomMinioClient(MinioAsyncClient.builder()
                .endpoint(publicEndpoint)
                .credentials(accessKey, secretKey)
                .build());

        minioClientPrivateIn = new CustomMinioClient(MinioAsyncClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build());

        minioClientPrivateNet = new CustomMinioClient(MinioAsyncClient.builder()
                .endpoint(publicEndpoint)
                .credentials(accessKey, secretKey)
                .build());

        // 创建桶（如果不存在）
        ensureBucket(minioClientPublicIn, bucketPublic, true);
        ensureBucket(minioClientPublicNet, bucketPublic, true);
        ensureBucket(minioClientPrivateIn, bucketPrivate, false);
        ensureBucket(minioClientPrivateNet, bucketPrivate, false);
    }


    @SneakyThrows
    private void ensureBucket(MinioAsyncClient client, String bucket, boolean publicRead) {
        // 判断桶是否存在（异步 → 同步等待）
        boolean exists = client.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        ).get();

        if (!exists) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build()).get();
            log.info("已创建桶：{}", bucket);
        }

        // 设置公共读策略
        if (publicRead) {
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

            client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(policy)
                    .build()
            ).get(); // ← 同步等待执行完成
            log.info("已为桶 [{}] 设置公共读策略", bucket);
        }
    }

    @Override
    @SneakyThrows
    public FileInfoDTO uploadFile(String fileKey, InputStream inputStream, long size, String contentType, String md5, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;
        MinioUtil.uploadFile(client, bucket, fileKey, inputStream, size, contentType);

        FsFileStorage fsFileStorage = new FsFileStorage();
        fsFileStorage.setBucket(bucket);
        fsFileStorage.setFileKey(fileKey);
        fsFileStorage.setStorageType(FileStorageConstant.MINIO_TYPE);
        fsFileStorage.setSize(size);
        fsFileStorage.setContentType(contentType);
        fsFileStorage.setUri(bucket + "/" + fileKey);
        fsFileStorage.setMd5(md5);
        fileStorageMapper.insert(fsFileStorage);

        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        BeanUtils.copyProperties(fsFileStorage, fileInfoDTO);
        fileInfoDTO.setUrl(getFileUrl(fileKey, isPublic));
        return fileInfoDTO;
    }

    @Override
    @SneakyThrows
    public void deleteFile(String fileKey, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;
        MinioUtil.deleteFile(client, bucket, fileKey);
        fileStorageMapper.delete(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getFileKey, fileKey));
    }

    @Override
    @SneakyThrows
    public InputStream downloadFile(String fileKey, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;
        return MinioUtil.getObject(client, bucket, fileKey, null, null);
    }

    @Override
    @SneakyThrows
    public String getFileUrl(String fileKey, boolean isPublic) {
        if (isPublic) {
            // 公共桶直接返回公网地址
            return publicEndpoint + "/" + bucketPublic + "/" + fileKey;
        } else {
            // 私有桶生成预签名 URL，公网访问
            return MinioUtil.getPreviewUrl(minioClientPrivateNet, bucketPrivate, fileKey);
        }
    }

    @Override
    @SneakyThrows
    public MultipartUploadInfo getPostPolicy(String fileKey, int expireSeconds, String uploadId, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = isPublic ? minioClientPublicNet : minioClientPrivateNet;
        return MinioUtil.getPostPolicy(client, bucket, fileKey, uploadId, expireSeconds);
    }

    @Override
    public FileInfoDTO complete(String fileKey, String contentType, String md5, long size, boolean isPublic) {
        return null;
    }

    @Override
    @SneakyThrows
    public MultipartUploadInfo getMultipartUploadInfo(String fileKey, long partSize, long fileSize, int expireSeconds, String uploadId, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = isPublic ? minioClientPublicNet : minioClientPrivateNet;
        return MinioUtil.initMultipartUpload(client, bucket, fileKey, partSize, fileSize, expireSeconds, uploadId);
    }

    @Override
    @SneakyThrows
    public FileInfoDTO mergeMultipartUpload(String uploadId, String fileKey, String md5, Long size, String contentType, boolean isPublic) {
        String bucket = isPublic ? bucketPublic : bucketPrivate;
        CustomMinioClient client = isPublic ? minioClientPublicNet : minioClientPrivateNet;
        MinioUtil.mergeMultipartUpload(client, bucket, fileKey, uploadId);


        FsFileStorage fsFileStorage = new FsFileStorage();
        fsFileStorage.setBucket(bucket);
        fsFileStorage.setFileKey(fileKey);
        fsFileStorage.setStorageType(FileStorageConstant.MINIO_TYPE);
        fsFileStorage.setUri(bucket + "/" + fileKey);
        fsFileStorage.setMd5(md5);
        fsFileStorage.setSize(size);
        fsFileStorage.setContentType(contentType);
        fileStorageMapper.insert(fsFileStorage);

        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        BeanUtils.copyProperties(fsFileStorage, fileInfoDTO);
        fileInfoDTO.setUrl(getFileUrl(fileKey, isPublic));
        return fileInfoDTO;
    }
}
