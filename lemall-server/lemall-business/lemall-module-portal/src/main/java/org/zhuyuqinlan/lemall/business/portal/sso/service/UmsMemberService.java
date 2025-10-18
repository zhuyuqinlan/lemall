package org.zhuyuqinlan.lemall.business.portal.sso.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.common.captcha.constant.CaptchaConstant;
import org.zhuyuqinlan.lemall.common.captcha.service.CaptchaService;
import org.zhuyuqinlan.lemall.common.constant.AuthConstant;
import org.zhuyuqinlan.lemall.common.entity.UmsMember;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberLevel;
import org.zhuyuqinlan.lemall.common.mapper.UmsMemberMapper;
import org.zhuyuqinlan.lemall.common.response.BizException;

@Slf4j
@Service
public class UmsMemberService extends ServiceImpl<UmsMemberMapper, UmsMember> {

    private final UmsMemberLevelService memberLevelService;
    private final CaptchaService captchaService;

    public UmsMemberService(UmsMemberLevelService memberLevelService,
                       @Qualifier("captchaEmailServiceImpl") CaptchaService captchaService) {
        this.memberLevelService = memberLevelService;
        this.captchaService = captchaService;
    }

    // ======================= 注册 =======================
    public void register(String username, String password, String email, String authCode) {
        if (!verifyAuthCode(email, authCode)) {
            throw new BizException("验证码错误");
        }

        UmsMember umsMember = super.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getUsername, username), false);
        if (umsMember != null) {
            throw new BizException("用户名重复");
        }

        UmsMemberLevel umsMemberLevel = memberLevelService.getOne(
                Wrappers.<UmsMemberLevel>lambdaQuery().eq(UmsMemberLevel::getDefaultStatus, 1), false
        );
        if (umsMemberLevel == null) {
            throw new BizException("请设置默认会员");
        }

        UmsMember member = new UmsMember();
        member.setUsername(username);
        member.setPassword(BCrypt.hashpw(password));
        member.setEmail(email);
        member.setMemberLevelId(umsMemberLevel.getId());
        member.setStatus(1);
        super.save(member);
    }

    private boolean verifyAuthCode(String email, String authCode) {
        String code = captchaService.getAuthCode(CaptchaConstant.EMAIL,email);
        return StringUtils.hasText(code) && code.equals(authCode);
    }

    // ======================= 登录 =======================

    public SaTokenInfo login(String username, String password) {
        UmsMember umsMember = super.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getUsername, username), false);
        if (umsMember == null) throw new BizException("用户不存在");
        if (!umsMember.getStatus().equals(1)) throw new BizException("该账号已被禁用");
        if (!BCrypt.checkpw(password, umsMember.getPassword())) throw new BizException("密码错误");

        StpMemberUtil.login(umsMember.getId());

        UmsMemberResponseDTO userDTO = new UmsMemberResponseDTO();
        userDTO.setUsername(umsMember.getUsername());
        userDTO.setId(umsMember.getId());
        StpMemberUtil.getSession().set(AuthConstant.STP_ADMIN_INFO, userDTO);

        return StpMemberUtil.getTokenInfo();
    }

    // ======================= 当前用户 =======================

    public UmsMemberResponseDTO getCurrentMember() {
        return (UmsMemberResponseDTO) StpMemberUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
    }

    // ======================= 登出 =======================

    public void logout() {
        StpMemberUtil.logout();
    }

    // ======================= 验证码 =======================

    public void generateAuthCode(String email) {
        if (!captchaService.canSendAuthCode(CaptchaConstant.EMAIL, email)) {
            throw new BizException("请勿频繁获取验证码");
        }
        String code = captchaService.setAuthCode(email, CaptchaConstant.EMAIL, 300, 60);
        log.info("验证码为：{}", code);
    }

    // ======================= 密码更新 =======================

    public void updatePassword(String email, String password, String authCode) {
        if (!verifyAuthCode(email, authCode)) throw new BizException("验证码错误");

        UmsMember member = super.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getEmail, email), false);
        if (member == null) throw new BizException("邮箱不存在");

        member.setPassword(BCrypt.hashpw(password));
        super.updateById(member);
    }
}
