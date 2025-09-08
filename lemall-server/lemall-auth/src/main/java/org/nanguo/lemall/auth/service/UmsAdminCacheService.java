package org.nanguo.lemall.auth.service;

import org.nanguo.lemall.common.dto.AdminUserDto;

public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);


    /**
     * 获取缓存后台用户信息
     */
    AdminUserDto getAdmin(Long adminId);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(AdminUserDto admin);
}
