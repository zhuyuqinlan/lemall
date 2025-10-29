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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * MinIO 文件上传业务服务（单文件 + 分片 + Redis缓存）
 */
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

    // 线程安全的日期格式
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public MinioFileService(@Qualifier("minioStorageService") CloudFileStorageService cloudFileStorageService,
                            RedisService redisService,
                            FileCacheService fileCacheService,
                            LemallFileConfig fileConfig) {
        this.cloudFileStorageService = cloudFileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
        this.fileConfig = fileConfig;
    }

    // ---------- 1. 生成 accessCode + 限流 ----------
    public String accessCode(String token, boolean isPublic) {
        // 限流：1分钟最多3次
        String limitKey = buildRedisKey(isPublic ? PUBLIC_LIMIT_KEY : PRIVATE_LIMIT_KEY, token, null);
        Long count = redisService.incr(limitKey, 1);
        if (count == 1) redisService.expire(limitKey, 60);
        if (count > 3) throw new RuntimeException("请求过于频繁，请稍后再试");

        // 生成 accessCode
        String accessCode = UUID.randomUUID().toString().replace("-", "");
        String accessKey = buildRedisKey(isPublic ? PUBLIC_ACCESS_KEY : PRIVATE_ACCESS_KEY, token, accessCode);
        redisService.set(accessKey, "1", FileStorageConstant.MINIO_ACCESS_EXPIRE);
        return accessCode;
    }

    // ---------- 2. 获取上传地址（单文件） ----------
    public MultipartUploadInfo uploadUrl(String token, String accessCode, String md5,
                                         String contentType, String fileName, String uploadId, boolean isPublic) {
        // 校验 accessCode
        checkAccessCode(token, accessCode, isPublic);

        // 检查缓存，避免重复上传
        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        if (cache != null) {
            MultipartUploadInfo dto = new MultipartUploadInfo();
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
            return dto;
        }

        // 校验文件类型
        String ext = validateFileType(fileName, contentType);

        // 生成 fileKey
        String fileKey = generateFileKey(ext);

        // 获取 MinIO PostPolicy
        MultipartUploadInfo postPolicy = cloudFileStorageService.getPostPolicy(fileKey, FileStorageConstant.POST_POLICY_EXPIRE, uploadId, isPublic);
        postPolicy.setExist(false);

        // 保存上传信息到 Redis
        Map<String, String> map = new HashMap<>();
        map.put("fileKey", fileKey);
        map.put("contentType", contentType);
        map.put("md5", md5);
        String uploadKey = buildRedisKey(isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY, token, postPolicy.getUploadId());
        redisService.hSetAll(uploadKey, map, FileStorageConstant.POST_POLICY_EXPIRE + 30);

        return postPolicy;
    }

    // ---------- 3. 完成上传 ----------
    public FileInfoDTO complete(String token, String uploadId, boolean isPublic) {
        Map<Object, Object> cacheMap = getUploadCache(token, uploadId, isPublic);
        String md5 = cacheMap.getOrDefault("md5", "").toString();
        String contentType = cacheMap.getOrDefault("contentType", "").toString();
        String fileKey = cacheMap.getOrDefault("fileKey", "").toString();
        return cloudFileStorageService.complete(fileKey, contentType, md5, isPublic);
    }

    // ---------- 4. 分片上传获取信息 ----------
    public MultipartUploadInfo multipartUploadInfoResult(String token, String accessCode, String uploadId, String md5,
                                                         String contentType, String fileName, long fileSize, boolean isPublic) {
        // 校验 accessCode
        checkAccessCode(token, accessCode, isPublic);

        if (fileSize < FileStorageConstant.MINIMUM_THRESHOLD_FOR_FILE_FRAGMENTATION) {
            throw new RuntimeException("分片过小，请直接用单次上传");
        }

        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        if (cache != null) {
            MultipartUploadInfo dto = new MultipartUploadInfo();
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
            return dto;
        }

        // 校验文件类型
        String ext = validateFileType(fileName, contentType);

        // 生成 fileKey
        String fileKey = generateFileKey(ext);

        // 获取 MinIO 分片上传信息
        MultipartUploadInfo postPolicy = cloudFileStorageService.getMultipartUploadInfo(fileKey, FileStorageConstant.FILE_FRAGMENT_SIZE,
                fileSize, FileStorageConstant.POST_POLICY_MULTIPART_EXPIRE, uploadId, isPublic);
        postPolicy.setExist(false);

        // 保存上传信息到 Redis
        Map<String, String> map = new HashMap<>();
        map.put("fileKey", fileKey);
        map.put("contentType", contentType);
        map.put("md5", md5);
        map.put("fileSize", String.valueOf(fileSize));
        String uploadKey = buildRedisKey(isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY, token, postPolicy.getUploadId());
        redisService.hSetAll(uploadKey, map, FileStorageConstant.POST_POLICY_EXPIRE + 30);

        return postPolicy;
    }

    // ---------- 5. 完成分片上传 ----------
    public FileInfoDTO completeMultipart(String token, String uploadId, boolean isPublic) {
        Map<Object, Object> cacheMap = getUploadCache(token, uploadId, isPublic);
        String md5 = cacheMap.getOrDefault("md5", "").toString();
        String contentType = cacheMap.getOrDefault("contentType", "").toString();
        String fileKey = cacheMap.getOrDefault("fileKey", "").toString();
        long fileSize = Long.parseLong(cacheMap.getOrDefault("fileSize", "0").toString());
        return cloudFileStorageService.mergeMultipartUpload(uploadId, fileKey, md5, fileSize, contentType, isPublic);
    }

    // ---------- 6. 校验 accessCode ----------
    private void checkAccessCode(String token, String accessCode, boolean isPublic) {
        String accessKey = buildRedisKey(isPublic ? PUBLIC_ACCESS_KEY : PRIVATE_ACCESS_KEY, token, accessCode);
        if (!redisService.hasKey(accessKey)) throw new RuntimeException("access已过期");
        redisService.del(accessKey);
    }

    // ---------- 7. 获取 Redis 上传缓存 ----------
    private Map<Object, Object> getUploadCache(String token, String uploadId, boolean isPublic) {
        String uploadKey = buildRedisKey(isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY, token, uploadId);
        if (!redisService.hasKey(uploadKey)) throw new RuntimeException("uploadId已过期");
        Map<Object, Object> cacheMap = redisService.hGetAll(uploadKey);
        redisService.del(uploadKey);
        return cacheMap;
    }

    // ---------- 8. 校验文件类型 ----------
    private String validateFileType(String fileName, String contentType) {
        Map<String, List<String>> mimeAllow = fileConfig.toMimeAllowMap();
        List<String> exts = mimeAllow.get(contentType);
        String ext = getFileExtension(fileName);

        if (exts == null || exts.isEmpty() || !exts.contains(ext.toLowerCase())) {
            throw new RuntimeException("文件类型不被允许：" + contentType + " / " + ext);
        }
        return ext;
    }

    // ---------- 9. 生成 fileKey ----------
    private String generateFileKey(String ext) {
        return DATE_FORMATTER.format(LocalDate.now()) + "/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
    }

    // ---------- 10. 工具方法：提取文件扩展名 ----------
    private String getFileExtension(String path) {
        if (!StringUtils.hasText(path)) return "";
        String filename = path;
        int sepIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (sepIndex != -1) filename = path.substring(sepIndex + 1);
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    // ---------- 11. 统一构建 Redis Key ----------
    private String buildRedisKey(String prefix, String token, String suffix) {
        String key = REDIS_PREFIX + ":" + prefix.replace("{token}", token);
        if (suffix != null) key = key.replace("{uploadId}", suffix).replace("{accessCode}", suffix);
        return key;
    }
}
