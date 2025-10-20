package org.zhuyuqinlan.lemall.common.captcha.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.service.RedisService;

/**
 * 验证码 Redis 操作实现
 * <p>
 * 功能：
 * 1. 存储验证码到 Redis，并设置过期时间
 * 2. 防刷：限制同一目标在一定时间内重复获取验证码
 */
@Service
public class CaptchaRedisService {

    private final RedisService redisService;

    public CaptchaRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    /** Redis 数据库前缀，用于隔离不同应用或业务 */
    @Value("${redis.database}")
    private String REDIS_DATABASE;

    /** 验证码 key 前缀 */
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;

    /** 验证码发送频率限制 key 前缀 */
    @Value("${redis.key.authCodeLimit}")
    private String REDIS_KEY_AUTH_CODE_LIMIT;

    /**
     * 设置验证码
     *
     * @param target 接收目标，例如邮箱或手机号
     * @param type 验证码类型，例如 "EMAIL"、"SMS" 等
     * @param authCode 验证码
     * @param expireSeconds 验证码过期时间（秒）
     * @param intervalSeconds 两次发送最小间隔时间（秒），用于防刷
     */
    public void setAuthCode(String target, String type, String authCode, long expireSeconds, long intervalSeconds) {
        // 构建验证码存储 key：格式 [数据库前缀]:[验证码前缀]:[类型]:[目标]
        String authCodeKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + type + ":" + target;
        // 存储验证码，设置过期时间
        redisService.set(authCodeKey, authCode, expireSeconds);

        // 构建防刷 key：格式 [数据库前缀]:[防刷前缀]:[类型]:[目标]
        String limitKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE_LIMIT + ":" + type + ":" + target;
        // 存储防刷标记，设置发送间隔时间
        redisService.set(limitKey, "1", intervalSeconds);
    }

    /**
     * 获取验证码
     *
     * @param target 接收目标
     * @param type 验证码类型
     * @return 验证码字符串，如果不存在返回 null
     */
    public String getAuthCode(String target, String type) {
        String authCodeKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + type + ":" + target;
        return (String) redisService.get(authCodeKey);
    }

    /**
     * 判断当前目标是否可以再次发送验证码
     *
     * @param target 接收目标
     * @param type 验证码类型
     * @return true 表示可以发送，false 表示间隔时间未到
     */
    public boolean canSendAuthCode(String target, String type) {
        String limitKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE_LIMIT + ":" + type + ":" + target;
        // 如果限制 key 不存在，说明可以发送
        return !redisService.hasKey(limitKey);
    }

}

