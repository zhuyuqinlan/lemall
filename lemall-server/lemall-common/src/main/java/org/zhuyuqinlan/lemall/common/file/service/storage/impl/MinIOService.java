package org.zhuyuqinlan.lemall.common.file.service.storage.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.dto.PostPolicyDTO;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.utils.MinIOUtils;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;

import java.io.InputStream;
import java.util.Map;

/**
 * MinIO Service（支持公共桶 + 私有桶 + 内网/公网访问）
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

    @Value("${minio.bucketName.public}")
    private String bucketPublic;

    @Value("${minio.bucketName.private}")
    private String bucketPrivate;

    private MinioClient minioClientPublicIn;   // 公共桶内网客户端
    private MinioClient minioClientPrivateIn;  // 私有桶内网客户端
    private MinioClient minioClientPublicNet;  // 公共桶公网客户端
    private MinioClient minioClientPrivateNet; // 私有桶公网客户端（生成公网预签名 URL）

    private final FsFileStorageMapper fileStorageMapper;

    public MinIOService(FsFileStorageMapper fileStorageMapper) {
        this.fileStorageMapper = fileStorageMapper;
    }

    /**
     * 初始化四个 MinioClient 并创建桶
     */
    @PostConstruct
    public void init() throws Exception {
        // 公共桶内网
        minioClientPublicIn = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        ensureBucket(minioClientPublicIn, bucketPublic, true);

        // 公共桶公网
        minioClientPublicNet = MinioClient.builder()
                .endpoint(publicEndpoint)
                .credentials(accessKey, secretKey)
                .build();

        // 私有桶内网
        minioClientPrivateIn = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        ensureBucket(minioClientPrivateIn, bucketPrivate, false);

        // 私有桶公网
        minioClientPrivateNet = MinioClient.builder()
                .endpoint(publicEndpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /* ----------------------- 上传 ----------------------- */
    @Override
    public FileInfoDTO uploadFile(String fileKey, InputStream inputStream, long size, String contentType, String md5, boolean isPublic) {
        try {
            String bucket = isPublic ? bucketPublic : bucketPrivate;
            MinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;
            MinIOUtils.uploadFile(client, bucket, fileKey, inputStream, size, contentType);

            FsFileStorage fsFileStorage = new FsFileStorage();
            fsFileStorage.setBucket(bucket);
            fsFileStorage.setFilekey(fileKey);
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /* ----------------------- 下载 ----------------------- */

    @Override
    public InputStream downloadFile(String fileKey, boolean isPublic) {
        try {
            String bucket = isPublic ? bucketPublic : bucketPrivate;
            MinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;
            return MinIOUtils.downloadFile(client, bucket, fileKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /* ----------------------- 获取文件 URL ----------------------- */

    @Override
    public String getFileUrl(String fileKey, boolean isPublic) {
        if (isPublic) {
            // 公共桶直接返回公网地址
            return publicEndpoint + "/" + bucketPublic + "/" + fileKey;
        } else {
            try {
                // 私有桶生成预签名 URL，公网访问
                return MinIOUtils.getPreviewUrl(minioClientPrivateNet, bucketPrivate, fileKey, 3600);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    /* ----------------------- 删除 ----------------------- */

    @Override
    public void deleteFile(String fileKey, boolean isPublic) {
        try {
            String bucket = isPublic ? bucketPublic : bucketPrivate;
            MinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;
            MinIOUtils.deleteFile(client, bucket, fileKey);
            fileStorageMapper.delete(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getFilekey, fileKey));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /* ----------------------- 前端直传凭证 ----------------------- */

    @Override
    public PostPolicyDTO getPostPolicy(String objectName, int expireSeconds, boolean isPublic) {
        try {
            String bucket = isPublic ? bucketPublic : bucketPrivate;
            MinioClient client = isPublic ? minioClientPublicNet : minioClientPrivateNet;
            return MinIOUtils.getPostPolicy(client, bucket, objectName, expireSeconds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartUploadInfo getMultipartUploadInfo(String fileKey, int expireSeconds, long partSize, long fileSize, boolean isPublic) {
        try {
            String bucket = isPublic ? bucketPublic : bucketPrivate;
            MinioClient minioClient = isPublic ? minioClientPublicNet : minioClientPrivateNet;
            MultipartUploadInfo multipartUploadInfo = MinIOUtils.initMultipartUpload(minioClient, bucket, fileKey, partSize, fileSize, expireSeconds);
            multipartUploadInfo.getParts().forEach(e -> e.remove("fileKey"));
            return multipartUploadInfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /* ----------------------- 保存文件记录 ----------------------- */

    @Override
    public FileInfoDTO saveFileRecord(String fileKey, boolean isPublic) {
        try {
            String bucket = isPublic ? bucketPublic : bucketPrivate;
            MinioClient client = isPublic ? minioClientPublicIn : minioClientPrivateIn;

            Map<String, Object> info = MinIOUtils.getFileInfo(client, bucket, fileKey);
            long size = Long.parseLong(info.get("size").toString());
            String contentType = info.get("contentType").toString();

            FsFileStorage fsFileStorage = new FsFileStorage();
            fsFileStorage.setBucket(bucket);
            fsFileStorage.setFilekey(fileKey);
            fsFileStorage.setStorageType(FileStorageConstant.MINIO_TYPE);
            fsFileStorage.setSize(size);
            fsFileStorage.setContentType(contentType);
            fsFileStorage.setUri(bucket + "/" + fileKey);
            fileStorageMapper.insert(fsFileStorage);
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            BeanUtils.copyProperties(fsFileStorage, fileInfoDTO);
            fileInfoDTO.setUrl(getFileUrl(fileKey, isPublic));
            return fileInfoDTO;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /* ----------------------- 辅助方法 ----------------------- */

    /**
     * 创建桶并设置策略
     *
     * @param client      MinioClient
     * @param bucket      桶名
     * @param publicRead  是否设置为公共读
     */
    private void ensureBucket(MinioClient client, String bucket, boolean publicRead) throws Exception {
        boolean found = client.bucketExists(io.minio.BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            client.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucket).build());
        }
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
            client.setBucketPolicy(io.minio.SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(policy)
                    .build());
        }
    }
}
