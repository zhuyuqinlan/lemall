package org.zhuyuqinlan.lemall.common.file.service.biz;

import jakarta.servlet.http.HttpServletResponse;
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
import org.zhuyuqinlan.lemall.common.file.dto.ext.FileInfoExistDTO;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileCacheService;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileStorageService;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 本地文件业务逻辑封装
 * 负责：
 * - 上传（含秒传）
 * - 下载 / 预览
 * - 删除
 */
@Slf4j
@Service
public class LocalFileService {

    private final FileStorageService fileStorageService;
    private final RedisService redisService;
    private final FileCacheService fileCacheService;
    private final LemallFileConfig fileConfig;

    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;

    @Value("${redis.key.fs.access.local.access}")
    private String REDIS_KEY_LOCAL_FILE_ACCESS;

    @Value("${redis.key.fs.preUpload.local}")
    private String REDIS_KEY_PREUPLOAD_LOCAL;

    @Value("${redis.key.fs.access.local.limit}")
    private String REDIS_KEY_LOCAL_LIMIT_LIMIT;

    @Value("${localFile.local-download-prefix}")
    private String localFileDownloadPrefix;

    public LocalFileService(
            @Qualifier("localFileStorageService") FileStorageService fileStorageService,
            RedisService redisService,
            FileCacheService fileCacheService,
            LemallFileConfig fileConfig) {
        this.fileStorageService = fileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
        this.fileConfig = fileConfig;
    }

    // ---------- 1. 获取上传 access code，限制1分钟5次 ----------
    public String getAccessCode(String token) {
        String limitKey = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_LIMIT_LIMIT.replace("{token}", token);
        Long count = redisService.incr(limitKey, 1);
        if (count == 1) redisService.expire(limitKey, 60);
        if (count > 5) throw new RuntimeException("请求过于频繁，请稍后再试");

        String accessCode = UUID.randomUUID().toString().replace("-", "");
        redisService.set(
                REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                        .replace("{token}", token)
                        .replace("{accessCode}", accessCode),
                "1",
                FileStorageConstant.LOCAL_ACCESS_EXPIRE
        );
        return accessCode;
    }

    // ---------- 2. 检查文件 MD5，实现秒传逻辑 ----------
    public FileInfoExistDTO checkFile(String token, String md5, String accessCode) {
        String key = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                .replace("{token}", token).replace("{accessCode}", accessCode);
        if (!redisService.hasKey(key)) throw new RuntimeException("access已过期");
        redisService.del(key);

        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        FileInfoExistDTO dto = new FileInfoExistDTO();
        if (cache == null) {
            String uploadCode = UUID.randomUUID().toString().replace("-", "");
            redisService.set(
                    REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                            .replace("{token}", token)
                            .replace("{uploadCode}", uploadCode),
                    md5,
                    FileStorageConstant.LOCAL_UPLOAD_EXPIRE
            );
            dto.setExist(false);
            dto.setUploadCode(uploadCode);
        } else {
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
        }
        return dto;
    }

    // ---------- 3. 上传文件（含 MD5 校验 + MIME 验证 + 秒传） ----------
    @SneakyThrows
    public FileInfoDTO uploadFile(String token, String uploadId, MultipartFile file) {
        // ---------- 3.1 校验 redis token ----------
        String md5 = redisService.get(
                REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                        .replace("{token}", token)
                        .replace("{uploadCode}", uploadId)
        );
        if (!StringUtils.hasText(md5)) throw new RuntimeException("uploadCode已过期");
        redisService.del(
                REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                        .replace("{token}", token)
                        .replace("{uploadCode}", uploadId)
        );

        // ---------- 3.2 验证文件类型 + MD5 ----------
        validateFile(file, md5);

        // ---------- 3.3 秒传逻辑 ----------
        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        if (cache != null) {
            FileInfoDTO dto = new FileInfoDTO();
            BeanUtils.copyProperties(cache, dto);
            dto.setMd5(md5);
            return dto;
        }

        // ---------- 3.4 生成存储路径 ----------
        String ext = getFileExtension(file.getOriginalFilename());
        String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String fileKey = dateFolder + "/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;

        // ---------- 3.5 上传文件 ----------
        FileInfoDTO fileInfoDTO;
        try (InputStream uploadStream = file.getInputStream()) {
            fileInfoDTO = fileStorageService.uploadFile(
                    fileKey,
                    uploadStream,
                    file.getSize(),
                    file.getContentType(),
                    md5
            );
        }

        return fileInfoDTO;
    }

    // ---------- 4. 下载或预览文件（浏览器访问） ----------
    @SneakyThrows
    public void serveFile(String requestURI, HttpServletResponse response) {
        String localPath = requestURI.replaceFirst("^" + localFileDownloadPrefix + "/", "");
        try (InputStream inputStream = fileStorageService.downloadFile(localPath)) {
            Path path = Paths.get(localPath);
            String contentType = Files.probeContentType(path);
            if (contentType == null) contentType = "application/octet-stream";

            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            String filename = URLEncoder.encode(path.getFileName().toString(), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "inline; filename=" + filename);

            inputStream.transferTo(response.getOutputStream());
        }
    }

    // ---------- 5. 删除文件 ----------
    public void deleteFile(String fileKey) {
        fileStorageService.deleteFile(fileKey);
    }

    // ---------- 6. 验证文件合法性（MIME + 扩展名 + MD5 校验） ----------
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
    private String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

}
