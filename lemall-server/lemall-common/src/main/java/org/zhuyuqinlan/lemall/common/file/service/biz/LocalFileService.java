package org.zhuyuqinlan.lemall.common.file.service.biz;

import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * 获取上传 access code，限制1分钟5次
     */
    public String getAccessCode(String token) {
        String limitKey = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_LIMIT_LIMIT.replace("{token}", token);
        Long count = redisService.incr(limitKey, 1);
        if (count == 1) redisService.expire(limitKey, 60);
        if (count > 5) throw new RuntimeException("请求过于频繁，请稍后再试");

        String accessCode = UUID.randomUUID().toString();
        redisService.set(
                REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                        .replace("{token}", token)
                        .replace("{accessCode}", accessCode),
                "1",
                FileStorageConstant.LOCAL_ACCESS_EXPIRE
        );
        return accessCode;
    }

    /**
     * 检查文件 MD5，实现秒传逻辑
     */
    public FileInfoExistDTO checkFile(String token, String md5, String accessCode) {
        String key = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                .replace("{token}", token).replace("{accessCode}", accessCode);
        if (!redisService.hasKey(key)) throw new RuntimeException("access已过期");
        redisService.del(key);

        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        FileInfoExistDTO dto = new FileInfoExistDTO();
        if (cache == null) {
            String uploadCode = UUID.randomUUID().toString();
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

    /**
     * 上传文件（含 MD5 校验 + MIME 验证 + 秒传）
     */
    public FileInfoDTO uploadFile(String token, String uploadCode, MultipartFile file) {
        String md5 = redisService.get(
                REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                        .replace("{token}", token)
                        .replace("{uploadCode}", uploadCode)
        );
        if (!StringUtils.hasText(md5)) throw new RuntimeException("uploadCode已过期");

        redisService.del(
                REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                        .replace("{token}", token)
                        .replace("{uploadCode}", uploadCode)
        );

        try (InputStream in = file.getInputStream()) {
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase()
                    : "";

            validateMimeAndExt(contentType, ext);

            String fileMd5 = DigestUtils.md5DigestAsHex(in);
            if (!md5.equals(fileMd5)) throw new RuntimeException("文件在上传过程中受损");

            FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(fileMd5);
            if (cache != null) {
                FileInfoDTO dto = new FileInfoDTO();
                BeanUtils.copyProperties(cache, dto);
                dto.setMd5(md5);
                return dto;
            }

            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fileKey = dateFolder + "/" + UUID.randomUUID() + "." + ext;

            try (InputStream inputStream = file.getInputStream()) {
                return fileStorageService.uploadFile(
                        fileKey, inputStream, file.getSize(),
                        contentType, fileMd5
                );
            }

        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            throw new RuntimeException("本地文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 下载或预览文件（浏览器访问）
     */
    public void serveFile(String requestURI, HttpServletResponse response) {
        String fileKey = requestURI.replaceFirst("^" + localFileDownloadPrefix + "/", "");
        try (InputStream inputStream = fileStorageService.downloadFile(fileKey)) {
            Path path = Paths.get(fileKey);
            String contentType = Files.probeContentType(path);
            if (contentType == null) contentType = "application/octet-stream";

            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            String filename = URLEncoder.encode(path.getFileName().toString(), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "inline; filename=" + filename);

            inputStream.transferTo(response.getOutputStream());
        } catch (Exception e) {
            log.error("读取本地文件失败", e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * 删除文件
     */
    public void deleteFile(String fileKey) {
        try {
            fileStorageService.deleteFile(fileKey);
        } catch (Exception e) {
            log.error("删除本地文件失败", e);
            throw new RuntimeException("删除本地文件失败：" + e.getMessage());
        }
    }

    /**
     * 验证 MIME 与扩展名匹配
     */
    private void validateMimeAndExt(String mime, String ext) {
        if (!StringUtils.hasText(mime) || !StringUtils.hasText(ext)) {
            throw new RuntimeException("无法识别文件类型");
        }
        Map<String, List<String>> mimeAllow = fileConfig.toMimeAllowMap();
        List<String> allowedExts = mimeAllow.get(mime);
        if (allowedExts == null || !allowedExts.contains(ext)) {
            throw new RuntimeException("文件类型与扩展名不匹配或不被允许：" + mime + "----后缀：" + ext);
        }
    }
}
