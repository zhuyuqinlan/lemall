package org.zhuyuqinlan.lemall.common.file.service.biz;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 本地文件上传业务逻辑封装
 * 主要职责：
 * - 控制文件上传频率
 * - 校验文件秒传（MD5）
 * - 验证文件类型合法性（MIME + 后缀）
 * - 生成上传凭证（uploadCode）
 * - 执行文件保存与缓存
 */
@Slf4j
@Service
public class LocalFileService {

    private final FileStorageService fileStorageService;
    private final RedisService redisService;
    private final FileCacheService fileCacheService;
    private final LemallFileConfig fileConfig;

    // Redis key 前缀
    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;

    // 本地文件 access code 的 Redis key 模板
    @Value("${redis.key.fs.access.local.localFileAccess}")
    private String REDIS_KEY_LOCAL_FILE_ACCESS;

    // 上传前的临时凭证 key 模板
    @Value("${redis.key.fs.preUpload.local}")
    private String REDIS_KEY_PREUPLOAD_LOCAL;

    // 控制 access code 请求频率的 key 模板
    @Value("${redis.key.fs.access.local.limit}")
    private String REDIS_KEY_LOCAL_LIMIT_LIMIT;

    public LocalFileService(@Qualifier("localFileStorageService") FileStorageService fileStorageService,
                            RedisService redisService,
                            FileCacheService fileCacheService,
                            LemallFileConfig fileConfig) {
        this.fileStorageService = fileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
        this.fileConfig = fileConfig;
    }

    /**
     * 获取本地上传 access code
     * - 用于防止频繁请求、非法上传
     * - 1分钟内最多请求5次
     *
     * @param token 用户token
     * @return 上传 access code
     */
    public String getAccessCode(String token) {
        // 频率限制：60秒最多5次
        String limitKey = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_LIMIT_LIMIT.replace("{token}", token);
        Long count = redisService.incr(limitKey, 1);
        if (count == 1) redisService.expire(limitKey, 60);
        if (count > 5) throw new RuntimeException("请求过于频繁，请稍后再试");

        // 生成 access code 并缓存（有效期：LOCAL_ACCESS_EXPIRE）
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
     * 检查文件 MD5（用于秒传逻辑）
     * - 校验 access code 有效性
     * - 查询文件是否已存在缓存
     * - 不存在则生成新的 uploadCode
     *
     * @param token      用户token
     * @param md5        文件MD5
     * @param accessCode 上传访问码
     * @return 文件是否存在 + 上传凭证
     */
    public FileInfoExistDTO checkFile(String token, String md5, String accessCode) {
        String key = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                .replace("{token}", token).replace("{accessCode}", accessCode);
        if (!redisService.hasKey(key)) throw new RuntimeException("access已过期");

        // access 用一次即废，防止重放
        redisService.del(key);

        // 查询 Redis 缓存的文件信息（根据 MD5 判断是否秒传）
        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        FileInfoExistDTO dto = new FileInfoExistDTO();

        // 文件不存在，生成 uploadCode 供后续上传使用
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
            // 文件已存在，直接返回信息（秒传成功）
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
        }
        return dto;
    }

    /**
     * 上传文件（含MD5校验 + MIME验证 + 秒传判断）
     * 步骤：
     * 1. 校验 uploadCode 有效性
     * 2. 验证 MIME 类型与文件后缀是否匹配
     * 3. 计算文件 MD5，防止传输损坏
     * 4. 如果文件已存在，则直接返回
     * 5. 上传文件并返回存储信息
     *
     * @param token      用户token
     * @param uploadCode 上传凭证
     * @param file       上传文件
     * @return 文件存储信息
     */
    public FileInfoDTO uploadFile(String token, String uploadCode, MultipartFile file) {
        // 获取 Redis 缓存中的 MD5
        String md5 = redisService.get(
                REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                        .replace("{token}", token)
                        .replace("{uploadCode}", uploadCode)
        );
        if (!StringUtils.hasText(md5)) throw new RuntimeException("uploadCode已过期");

        // 用后即删，防止重复上传
        redisService.del(
                REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                        .replace("{token}", token)
                        .replace("{uploadCode}", uploadCode)
        );

        try (InputStream in = file.getInputStream()) {
            // 获取文件 MIME 和后缀
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase()
                    : "";

            // 验证 MIME 与后缀是否匹配
            validateMimeAndExt(contentType, ext);

            // 计算文件实际 MD5 校验完整性
            String fileMd5 = DigestUtils.md5DigestAsHex(in);
            if (!md5.equals(fileMd5)) throw new RuntimeException("文件在上传过程中受损");

            // 如果文件在缓存中已存在，直接返回（秒传成功）
            FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(fileMd5);
            if (cache != null) {
                FileInfoDTO dto = new FileInfoDTO();
                BeanUtils.copyProperties(cache, dto);
                dto.setMd5(md5);
                return dto;
            }

            // 生成存储路径（日期 + 随机名）
            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String objectName = dateFolder + "/" + UUID.randomUUID() + "." + ext;

            // 上传文件到本地存储实现
            try (InputStream inputStream = file.getInputStream()) {
                return fileStorageService.uploadFile(
                        objectName, inputStream, file.getSize(),
                        contentType, fileMd5
                );
            }

        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            throw new RuntimeException("本地文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 验证 MIME 类型与文件后缀是否匹配
     *
     * @param mime 文件 MIME 类型
     * @param ext  文件扩展名（不带点）
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
