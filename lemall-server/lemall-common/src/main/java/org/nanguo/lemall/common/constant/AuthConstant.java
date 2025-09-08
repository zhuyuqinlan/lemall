package org.nanguo.lemall.common.constant;

/**
 * 权限相关常量
 */
public interface AuthConstant {

    /**
     * Redis缓存权限规则（路径->资源）
     */
    String PATH_RESOURCE_MAP = "auth:pathResourceMap";

    /**
     * 会员登录类型
     */
    String STP_MEMBER_LOGIN_TYPE = "member";

    /**
     *  后台管理用户登录类型
     */
    String STP_ADMIN_LOGIN_TYPE = "login";

    /**
     * sa-token session中存储的会员信息
     */
    String STP_MEMBER_INFO = "memberInfo";

    /**
     * sa-token session中存储的后台管理员信息
     */
    String STP_ADMIN_INFO = "adminInfo";
}
