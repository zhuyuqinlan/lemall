package org.zhuyuqinlan.lemall.business.portal.sso.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.service.RedisService;

/**
 * 会员缓存服务（验证码等），直接合并接口和实现类
 */
@Service
public class UmsMemberCacheService {

    private final RedisService redisService;

    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE; // 5分钟

    @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;

    @Value("${redis.expire.authCodeLimit}")
    private Long REDIS_EXPIRE_AUTH_CODE_LIMIT;

    @Value("${redis.key.authCodeLimit}")
    private String REDIS_KEY_AUTH_CODE_LIMIT;

    public UmsMemberCacheService(RedisService redisService) {
        this.redisService = redisService;
    }

    // ======================= 验证码操作 =======================

    public void setAuthCode(String email, String authCode) {
        // 验证码 key
        String authCodeKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + email;
        redisService.set(authCodeKey, authCode, REDIS_EXPIRE_AUTH_CODE);

        // 发送频率限制 key
        String limitKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE_LIMIT + ":" + email;
        redisService.set(limitKey, "1", REDIS_EXPIRE_AUTH_CODE_LIMIT);
    }

    public String getAuthCode(String email) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + email;
        return (String) redisService.get(key);
    }

    public boolean canSendAuthCode(String email) {
        String limitKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE_LIMIT + ":" + email;
        return !redisService.hasKey(limitKey); // 如果限制 key 不存在，说明可以发送
    }
}
