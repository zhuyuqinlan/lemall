package org.zhuyuqinlan.lemall.common.file.service.biz;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.config.LemallFileConfig;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileCacheService;
import org.zhuyuqinlan.lemall.common.file.utils.MinioUtil;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.io.InputStream;
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

    public MultipartUploadInfo uploadUrl(String token, String accessCode, String md5, String contentType, String uploadId,boolean isPublic) {
        String accessKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_ACCESS_KEY : PRIVATE_ACCESS_KEY).replace("{token}", token)
                .replace("{accessCode}", accessCode);
        if (!redisService.hasKey(accessKey)) throw new RuntimeException("access已过期");
        redisService.del(accessKey);

        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        MultipartUploadInfo dto = new MultipartUploadInfo();
        if (cache == null) {

            Map<String, List<String>> mimeAllow = fileConfig.toMimeAllowMap();
            if (mimeAllow == null || !mimeAllow.containsKey(contentType)) {
                throw new RuntimeException("文件类型不被允许 ---- 后缀：" + contentType);
            }

            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fileKey = dateFolder + "/" + UUID.randomUUID().toString().replace("-", "") + "." + contentType;
            MultipartUploadInfo postPolicy = cloudFileStorageService.getPostPolicy(fileKey, FileStorageConstant.POST_POLICY_EXPIRE, uploadId,isPublic);
            postPolicy.setExist(false);
            String uploadKey = REDIS_PREFIX + ":" + (isPublic ? PUBLIC_UPLOAD_KEY : PRIVATE_UPLOAD_KEY)
                    .replace("{token}", token).replace("{uploadId}", uploadId);

            Map<String,String> map = new HashMap<>();
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

        // 从minio读取文件信息



        return null;
    }

    public MultipartUploadInfo multipartUploadInfoResult(String token, String accessCode, String md5, boolean isPublic) {
        return null;
    }

    public FileInfoDTO completeMultipart(String token, String uploadId, boolean isPublic) {
        return null;
    }

    @SneakyThrows
    private void validateFile(MultipartFile file, String expectedMd5) {
        String mime = file.getContentType();
        String ext = getFileExtension(file.getOriginalFilename());

        // MIME + 扩展名校验
        if (!StringUtils.hasText(mime) || !StringUtils.hasText(ext)) {
            throw new RuntimeException("无法识别文件类型");
        }
        Map<String, List<String>> mimeAllow = fileConfig.toMimeAllowMap();
        List<String> allowedExts = mimeAllow.get(mime);
        if (allowedExts == null || !allowedExts.contains(ext)) {
            throw new RuntimeException("文件类型与扩展名不匹配或不被允许：" + mime + "----后缀：" + ext);
        }

        // MD5 校验
        try (InputStream in = file.getInputStream()) {
            String fileMd5 = DigestUtils.md5DigestAsHex(in);
            if (!expectedMd5.equals(fileMd5)) {
                throw new RuntimeException("文件在上传过程中受损");
            }
        }
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
