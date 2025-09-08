package org.nanguo.lemall.business.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nanguo.lemall.common.entity.UmsAdmin;
import org.nanguo.lemall.common.entity.UmsPermission;
import org.nanguo.lemall.common.entity.UmsResource;
import org.nanguo.lemall.common.entity.UmsRole;

import java.util.List;

public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {

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