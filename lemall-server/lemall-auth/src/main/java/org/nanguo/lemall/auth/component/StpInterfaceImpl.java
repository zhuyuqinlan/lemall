package org.nanguo.lemall.auth.component;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.auth.service.UmsAdminCacheService;
import org.nanguo.lemall.common.constant.AuthConstant;
import org.nanguo.lemall.common.dto.AdminUserDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 *  自定义权限验证接口扩展
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UmsAdminCacheService umsAdminCacheService;

    private AdminUserDto getAdminUser(String loginType) {
        // 后台管理用户返回权限信息
        if (AuthConstant.STP_ADMIN_LOGIN_TYPE.equals(loginType)) {
            return umsAdminCacheService.getAdmin((Long) StpUtil.getLoginId());
        }
        return null;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        AdminUserDto user = getAdminUser(loginType);
        return user == null ? Collections.emptyList() : user.getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        AdminUserDto user = getAdminUser(loginType);
        return user == null ? Collections.emptyList() : user.getRoleList();
    }
}

