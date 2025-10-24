package org.zhuyuqinlan.lemall.common.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.util.concurrent.ThreadLocalRandom;


/**
 * 用于文件操作url
 */
@Service
@RequiredArgsConstructor
public class FileUrlCacheService {

    private final RedisService redisService;
    private final FsFileStorageMapper fileStorageMapper;

    // redis key信息
    @Value("${redis.database}")
    private String DATABASE;
    @Value("${redis.key.fileUrl}")
    private String FILE_URL_KEY;
    @Value("${redis.key.fileLock}")
    private String FILE_LOCK_KEY;

    // 前缀信息
    @Value("${minio.publicEndpoint}")
    private String PUBLIC_ENDPOINT;
    @Value("${localFile.access-url-prefix}")
    private String LOCAL_FILE_URL_PREFIX;

    /**
     * 获取文件访问URL（带缓存、带防护）
     * 1. 优先读缓存（防雪崩）
     * 2. 缓存未命中时加分布式锁（防击穿）
     * 3. 文件不存在时缓存空值（防穿透）
     */
    public String getFileUrl(Long fileId) {
        String cacheKey = DATABASE + ":" + FILE_URL_KEY + ":" + fileId;

        // 优先读缓存
        Object cached = redisService.get(cacheKey);
        if (cached != null) {
            String val = cached.toString();
            // 缓存了空值（文件不存在）
            if ("NULL".equals(val)) {
                return null;
            }
            return val;
        }

        // 防击穿：加分布式锁，避免高并发下多线程同时回源数据库
        String lockKey = DATABASE + ":" + FILE_LOCK_KEY + ":" + fileId;
        boolean locked = redisService.tryLock(lockKey, 10); // 锁10秒
        if (!locked) {
            // 没抢到锁的线程稍等后再读缓存
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            Object retry = redisService.get(cacheKey);
            return retry != null && !"NULL".equals(retry.toString()) ? retry.toString() : null;
        }

        try {
            // 双重检查，防止并发间缓存已被填充
            Object secondCheck = redisService.get(cacheKey);
            if (secondCheck != null) {
                String val = secondCheck.toString();
                if ("NULL".equals(val)) {
                    return null;
                }
                return val;
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
     * 根据存储类型拼接URL
     */
    private String buildFileUrl(FsFileStorage file) {
        return switch (file.getStorageType().toUpperCase()) {
            case "MINIO" -> PUBLIC_ENDPOINT + "/" + file.getBucket() + "/" + file.getUri();
            case "LOCAL" -> LOCAL_FILE_URL_PREFIX + "/" + file.getUri();
            default -> throw new RuntimeException("未知存储类型: " + file.getStorageType());
        };
    }
}

