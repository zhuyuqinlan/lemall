package org.nanguo.lemall.business.admin.system.service;

import org.nanguo.lemall.common.entity.UmsAdmin;

public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);


    /**
     * 获取缓存后台用户信息
     */
    UmsAdmin getAdmin(Long adminId);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UmsAdmin admin);
}
