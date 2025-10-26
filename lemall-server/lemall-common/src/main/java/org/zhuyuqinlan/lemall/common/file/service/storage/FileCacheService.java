package org.zhuyuqinlan.lemall.common.file.service.storage;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.util.concurrent.ThreadLocalRandom;


/**
 * 用于文件redis操作
 */
@Service
@RequiredArgsConstructor
public class FileCacheService {

    private final RedisService redisService;
    private final FsFileStorageMapper fileStorageMapper;

    // redis key信息
    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;
    @Value("${redis.key.fs.url.cacheByFileId}")
    private String FS_URL_BY_FILE_ID;
    @Value("${redis.key.fs.url.lockByFileId}")
    private String FS_URL_LOCK_BY_FILE_ID;
    @Value("${redis.key.fs.info.cacheByMd5}")
    private String FS_INFO_CACHE_BY_MD5;
    @Value("${redis.key.fs.info.lockByMd5}")
    private String FS_INFO_LOCK_BY_MD5;

    // 前缀信息
    @Value("${minio.publicEndpoint}")
    private String PUBLIC_ENDPOINT;
    @Value("${localFile.access-url-prefix}")
    private String LOCAL_FILE_URL_PREFIX;

    /**
     * 根据文件id获取文件访问URL（带缓存、带防护）
     * 1. 优先读缓存（防雪崩）
     * 2. 缓存未命中时加分布式锁（防击穿）
     * 3. 文件不存在时缓存空值（防穿透）
     */
    public String getFileUrlByFileId(Long fileId) {
        String cacheKey = REDIS_PREFIX + ":" + FS_URL_BY_FILE_ID.replace("{fileId}",fileId.toString());

        // 优先读缓存
        String cached = redisService.get(cacheKey);
        if (cached != null) {
            // 缓存了空值（文件不存在）
            if ("NULL".equals(cached)) {
                return null;
            }
            return cached;
        }

        // 防击穿：加分布式锁，避免高并发下多线程同时回源数据库
        String lockKey = REDIS_PREFIX + ":" + FS_URL_LOCK_BY_FILE_ID.replace("{fileId}",fileId.toString());
        boolean locked = redisService.tryLock(lockKey, 10); // 锁10秒
        if (!locked) {
            // 没抢到锁的线程稍等后再读缓存
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
            String retry = redisService.get(cacheKey);
            return retry != null && !"{}".equals(retry) ? retry : null;
        }

        try {
            // 双重检查，防止并发间缓存已被填充
            String secondCheck = redisService.get(cacheKey);
            if (secondCheck != null) {
                if ("NULL".equals(secondCheck)) {
                    return null;
                }
                return secondCheck;
            }

            // 查询数据库
            FsFileStorage file = fileStorageMapper.selectById(fileId);
            if (file == null) {
                // 防穿透：缓存空值 30s
                redisService.set(cacheKey, "NULL", 30);
                return null;
            }

            // 拼接URL
            String url = buildFileUrl(file);

            // 防雪崩：设置随机过期时间，1小时±5分钟
            int ttl = 3600 + ThreadLocalRandom.current().nextInt(300);
            redisService.set(cacheKey, url, ttl);

            return url;

        } finally {
            // 释放锁
            redisService.unlock(lockKey);
        }
    }

    /**
     * 根据文件 MD5 获取文件信息（带缓存、带防护）
     * 1. 优先读缓存（防雪崩）
     * 2. 缓存未命中时加分布式锁（防击穿）
     * 3. 文件不存在时缓存空值（防穿透）
     */
    public FileInfoCacheByMd5DTO getFileInfoByMd5(String md5) {
        String cacheKey = REDIS_PREFIX + ":" + FS_INFO_CACHE_BY_MD5.replace("{md5}", md5);

        // 优先读缓存
        String cached = redisService.get(cacheKey);
        if (cached != null) {
            if ("NULL".equals(cached)) {
                return null; // 防穿透空值
            }
            return JSONUtil.toBean(cached, FileInfoCacheByMd5DTO.class);
        }

        // 防击穿：加锁
        String lockKey = REDIS_PREFIX + ":" + FS_INFO_LOCK_BY_MD5.replace("{md5}", md5);
        boolean locked = redisService.tryLock(lockKey, 10); // 锁10秒
        if (!locked) {
            // 没抢到锁的线程稍等再读缓存
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
            String retry = redisService.get(cacheKey);
            return retry != null && !"NULL".equals(retry)
                    ? JSONUtil.toBean(retry, FileInfoCacheByMd5DTO.class) : null;
        }

        try {
            // 双重检查，防止并发间缓存已被填充
            String secondCheck = redisService.get(cacheKey);
            if (secondCheck != null) {
                if ("NULL".equals(secondCheck)) {
                    return null;
                }
                return JSONUtil.toBean(secondCheck, FileInfoCacheByMd5DTO.class);
            }

            // 查数据库
            FsFileStorage file = fileStorageMapper
                    .selectOne(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getMd5,md5),false);
            if (file == null) {
                // 防穿透缓存空值 30秒
                redisService.set(cacheKey, "NULL", 30);
                return null;
            }

            // 构建 DTO
            FileInfoCacheByMd5DTO dto = new FileInfoCacheByMd5DTO();
            BeanUtils.copyProperties(file,dto);
            dto.setUrl(buildFileUrl(file));
            // 防雪崩：随机过期时间（1小时±5分钟）
            int ttl = 3600 + ThreadLocalRandom.current().nextInt(300);
            String str = JSONUtil.toJsonStr(dto);
            redisService.set(cacheKey, str, ttl);

            return dto;

        } finally {
            redisService.unlock(lockKey);
        }
    }

    /**
     * 根据存储类型拼接URL
     */
    private String buildFileUrl(FsFileStorage file) {
        return switch (file.getStorageType().toUpperCase()) {
            case "MINIO" -> PUBLIC_ENDPOINT + "/" + file.getUri();
            case "LOCAL" -> LOCAL_FILE_URL_PREFIX + "/" + file.getUri();
            default -> throw new RuntimeException("未知存储类型: " + file.getStorageType());
        };
    }
}

