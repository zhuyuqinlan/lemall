package org.nanguo.lemall.auth.service.impl;

import org.nanguo.lemall.auth.service.UmsAdminCacheService;
import org.nanguo.lemall.common.dto.AdminUserDto;
import org.nanguo.lemall.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {
    @Autowired
    private RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Override
    public void delAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public AdminUserDto getAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + adminId;
        return (AdminUserDto) redisService.get(key);
    }

    @Override
    public void setAdmin(AdminUserDto admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getId();
        redisService.set(key, admin, REDIS_EXPIRE);
    }
}
