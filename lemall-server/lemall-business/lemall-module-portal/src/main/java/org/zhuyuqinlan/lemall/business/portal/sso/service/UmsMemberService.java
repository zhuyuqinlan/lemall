package org.zhuyuqinlan.lemall.business.portal.sso.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsMember;
import com.baomidou.mybatisplus.extension.service.IService;
public interface UmsMemberService extends IService<UmsMember>{


    /**
     * 会员注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param authCode 验证码
     */
    void register(String username, String password, String email, String authCode);

    /**
     * 会员登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    SaTokenInfo login(String username, String password);

    /**
     * 获取当前会员信息
     * @return 信息
     */
    UmsMemberResponseDTO getCurrentMember();

    /**
     * 登出
     */
    void logout();

    /**
     * 获取验证码
     * @param email 邮箱
     * @return 验证码
     */
    void generateAuthCode(String email);

    /**
     * 修改密码
     * @param email 邮箱
     * @param password 新密码
     * @param authCode 验证码
     */
    void updatePassword(String email, String password, String authCode);
}
