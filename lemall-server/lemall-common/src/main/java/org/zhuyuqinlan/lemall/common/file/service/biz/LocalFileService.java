package org.zhuyuqinlan.lemall.common.file.service.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.UUID;

/**
 * 本地文件上传业务逻辑封装
 * 主要职责：
 * - 控制文件上传频率
 * - 校验文件秒传（MD5）
 * - 生成上传凭证（uploadCode）
 * - 执行文件保存与缓存
 */
@Slf4j
@Service
public class LocalFileService {

    private final FileStorageService fileStorageService;
    private final RedisService redisService;
    private final FileCacheService fileCacheService;

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

    public LocalFileService(@Qualifier("localFileStorageService") FileStorageService fileStorageService, RedisService redisService, FileCacheService fileCacheService) {
        this.fileStorageService = fileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
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
     * 上传文件（含MD5校验 + 秒传判断）
     * 步骤：
     * 1. 校验 uploadCode 有效性
     * 2. 计算文件 MD5，防止传输损坏
     * 3. 如果文件已存在，则直接返回
     * 4. 上传文件并返回存储信息
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
            String originalFilename = file.getOriginalFilename();
            String suffix = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : "";
            String objectName = dateFolder + "/" + UUID.randomUUID() + suffix;

            // 上传文件到本地存储实现
            try (InputStream inputStream = file.getInputStream()) {
                return fileStorageService.uploadFile(
                        objectName, inputStream, file.getSize(),
                        file.getContentType(), fileMd5
                );
            }

        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            throw new RuntimeException("本地文件上传失败：" + e.getMessage());
        }
    }
}
