package org.zhuyuqinlan.lemall.business.portal.sso.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberCacheService;
import org.zhuyuqinlan.lemall.common.service.RedisService;


@Service
public class UmsMemberCacheServiceImpl implements UmsMemberCacheService {

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

    public UmsMemberCacheServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void setAuthCode(String email, String authCode) {
        // 验证码 key
        String authCodeKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + email;
        redisService.set(authCodeKey, authCode, REDIS_EXPIRE_AUTH_CODE);

        // 发送频率限制 key
        String limitKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE_LIMIT + ":" + email;
        redisService.set(limitKey, "1", REDIS_EXPIRE_AUTH_CODE_LIMIT);
    }

    @Override
    public String getAuthCode(String email) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + email;
        return (String) redisService.get(key);
    }

    @Override
    public boolean canSendAuthCode(String email) {
        String limitKey = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE_LIMIT + ":" + email;
        return !redisService.hasKey(limitKey); // 如果限制 key 不存在，说明可以发送
    }
}

