package org.zhuyuqinlan.lemall.common.file.service.biz;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.common.file.config.LemallFileConfig;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileCacheService;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class MinioFileService {
    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;
    @Value("${redis.key.fs.access.minio.public.access}")
    private String PUBLIC_ACCESS_KEY;
    @Value("${redis.key.fs.access.minio.private.access}")
    private String PRIVATE_ACCESS_KEY;
    @Value("${redis.key.fs.access.minio.public.limit}")
    private String PUBLIC_LIMIT_KEY;
    @Value("${redis.key.fs.access.minio.private.limit}")
    private String PRIVATE_LIMIT_KEY;
    @Value("${redis.key.fs.preUpload.minio.public}")
    private String PUBLIC_UPLOAD_KEY;
    @Value("${redis.key.fs.preUpload.minio.private}")
    private String PRIVATE_UPLOAD_KEY;

    private final CloudFileStorageService cloudFileStorageService;
    private final RedisService redisService;
    private final FileCacheService fileCacheService;
    private final LemallFileConfig fileConfig;

    public MinioFileService(@Qualifier("minioStorageService") CloudFileStorageService cloudFileStorageService,
                            RedisService redisService,
                            FileCacheService fileCacheService,
                            LemallFileConfig fileConfig) {
        this.cloudFileStorageService = cloudFileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
        this.fileConfig = fileConfig;
    }

    public String accessCode(String token, boolean isPublic) {
        String limitKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_LIMIT_KEY : PRIVATE_LIMIT_KEY).replace("{token}", token);
        Long count = redisService.incr(limitKey, 1);
        if (count == 1) redisService.expire(limitKey, 60);
        if (count > 3) throw new RuntimeException("请求过于频繁，请稍后再试");

        String accessCode = UUID.randomUUID().toString().replace("-", "");
        String accessKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_ACCESS_KEY : PRIVATE_ACCESS_KEY).replace("{token}", token)
                .replace("{accessCode}", accessCode);
        redisService.set(accessKey, "1", FileStorageConstant.MINIO_ACCESS_EXPIRE);
        return accessCode;
    }

    public MultipartUploadInfo uploadUrl(String token, String accessCode, String md5, String contentType, String fileName, String uploadId, boolean isPublic) {
        String accessKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_ACCESS_KEY : PRIVATE_ACCESS_KEY).replace("{token}", token)
                .replace("{accessCode}", accessCode);
        if (!redisService.hasKey(accessKey)) throw new RuntimeException("access已过期");
        redisService.del(accessKey);

        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        MultipartUploadInfo dto = new MultipartUploadInfo();
        if (cache == null) {

            Map<String, List<String>> mimeAllow = fileConfig.toMimeAllowMap();
            List<String> exts = mimeAllow.get(contentType);
            String ext = getFileExtension(fileName);

            if (exts == null || exts.isEmpty() || !exts.contains(ext.toLowerCase())) {
                throw new RuntimeException("文件类型不被允许：" + contentType + " / " + ext);
            }

            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fileKey = dateFolder + "/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
            MultipartUploadInfo postPolicy = cloudFileStorageService.getPostPolicy(fileKey, FileStorageConstant.POST_POLICY_EXPIRE, uploadId, isPublic);
            postPolicy.setExist(false);
            String uploadKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY)
                    .replace("{token}", token).replace("{uploadId}", postPolicy.getUploadId());

            Map<String, String> map = new HashMap<>();
            map.put("fileKey", fileKey);
            map.put("contentType", contentType);
            map.put("md5", md5);
            redisService.hSetAll(uploadKey, map, FileStorageConstant.POST_POLICY_EXPIRE + 30);
            return postPolicy;
        } else {
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
            return dto;
        }
    }

    public FileInfoDTO complete(String token, String uploadId, boolean isPublic) {
        String uploadKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY)
                .replace("{token}", token).replace("{uploadId}", uploadId);
        if (!redisService.hasKey(uploadKey)) throw new RuntimeException("uploadId已过期");

        // 拿到上传信息
        Map<Object, Object> cacheMap = redisService.hGetAll(uploadKey);
        String md5 = cacheMap.getOrDefault("md5", "").toString();
        String contentType = cacheMap.getOrDefault("contentType", "").toString();
        String fileKey = cacheMap.getOrDefault("fileKey", "").toString();
        redisService.del(uploadKey);

        // 校验并入库
        return cloudFileStorageService.complete(fileKey, contentType, md5, isPublic);
    }

    public MultipartUploadInfo multipartUploadInfoResult(String token, String accessCode, String uploadId, String md5, String contentType, String fileName, long fileSize, boolean isPublic) {
        String accessKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_ACCESS_KEY : PRIVATE_ACCESS_KEY).replace("{token}", token)
                .replace("{accessCode}", accessCode);
        if (!redisService.hasKey(accessKey)) throw new RuntimeException("access已过期");
        redisService.del(accessKey);

        if (fileSize < FileStorageConstant.MINIMUM_THRESHOLD_FOR_FILE_FRAGMENTATION) {
            throw new RuntimeException("分片过小；请直接用单次上传");
        }
        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        MultipartUploadInfo dto = new MultipartUploadInfo();
        if (cache == null) {

            Map<String, List<String>> mimeAllow = fileConfig.toMimeAllowMap();
            List<String> exts = mimeAllow.get(contentType);
            String ext = getFileExtension(fileName);

            if (exts == null || exts.isEmpty() || !exts.contains(ext.toLowerCase())) {
                throw new RuntimeException("文件类型不被允许：" + contentType + " / " + ext);
            }

            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fileKey = dateFolder + "/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;

            MultipartUploadInfo postPolicy = cloudFileStorageService.getMultipartUploadInfo(fileKey, FileStorageConstant.FILE_FRAGMENT_SIZE, fileSize,
                    FileStorageConstant.POST_POLICY_MULTIPART_EXPIRE, uploadId, isPublic);
            postPolicy.setExist(false);
            String uploadKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY)
                    .replace("{token}", token).replace("{uploadId}", postPolicy.getUploadId());

            Map<String, String> map = new HashMap<>();
            map.put("fileKey", fileKey);
            map.put("contentType", contentType);
            map.put("md5", md5);
            map.put("fileSize", String.valueOf(fileSize));
            redisService.hSetAll(uploadKey, map, FileStorageConstant.POST_POLICY_EXPIRE + 30);
            return postPolicy;
        } else {
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
            return dto;
        }
    }

    public FileInfoDTO completeMultipart(String token, String uploadId, boolean isPublic) {
        String uploadKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY)
                .replace("{token}", token).replace("{uploadId}", uploadId);
        if (!redisService.hasKey(uploadKey)) throw new RuntimeException("uploadId已过期");

        // 拿到上传信息
        Map<Object, Object> cacheMap = redisService.hGetAll(uploadKey);
        String md5 = cacheMap.getOrDefault("md5", "").toString();
        String contentType = cacheMap.getOrDefault("contentType", "").toString();
        String fileKey = cacheMap.getOrDefault("fileKey", "").toString();
        long fileSize = Long.parseLong(cacheMap.getOrDefault("fileSize", "").toString());
        redisService.del(uploadKey);

        // 合并
        return cloudFileStorageService.mergeMultipartUpload(uploadId, fileKey, md5, fileSize, contentType, isPublic);
    }


    // ---------- 7. 工具方法：提取文件扩展名 ----------
    private String getFileExtension(String path) {
        if (!StringUtils.hasText(path)) return "";

        // 先取文件名部分
        String filename = path;
        int sepIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (sepIndex != -1) {
            filename = path.substring(sepIndex + 1);
        }

        // 再取扩展名
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) return "";

        return filename.substring(dotIndex + 1).toLowerCase();
    }

}
