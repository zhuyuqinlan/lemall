package org.zhuyuqinlan.lemall.business.admin.system.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.common.entity.UmsPermission;
import org.zhuyuqinlan.lemall.common.entity.UmsResource;
import org.zhuyuqinlan.lemall.common.entity.UmsRole;

import java.util.List;

public interface UmsAdminDao {

    /**
     * 获取资源列表
     * @param adminId 用户id
     * @return 资源列表
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);


    /**
     * 获取用于所有角色
     */
    List<UmsRole> getRoleList(Long id);

    /**
     * 获取权限列表
     * @param id 用户id
     * @return 权限列表
     */
    List<UmsPermission> getPermissionList(@Param("id") Long id);
}