package org.zhuyuqinlan.lemall.business.portal.sso.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberCacheService;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberLevelService;
import org.zhuyuqinlan.lemall.common.constant.AuthConstant;
import org.zhuyuqinlan.lemall.common.entity.UmsMember;
import org.zhuyuqinlan.lemall.common.mapper.UmsMemberMapper;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberLevel;
import org.zhuyuqinlan.lemall.common.response.BizException;


@Service
@RequiredArgsConstructor
@Slf4j
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService{

    private final UmsMemberLevelService memberLevelService;
    private final UmsMemberCacheService memberCacheService;

    @Override
    public void register(String username, String password, String email, String authCode) {
        //1. 检查验证码
        if (!verifyAuthCode(email,authCode)) {
            throw new BizException("验证码错误");
        }
        //2. 检查用户名是否重复
        UmsMember umsMember = super.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getUsername, username),false);
        if (umsMember != null) {
            throw new BizException("用户名重复");
        }
        //3. 获取默认会员等级
        UmsMemberLevel umsMemberLevel = memberLevelService.getOne(Wrappers.<UmsMemberLevel>lambdaQuery().eq(UmsMemberLevel::getDefaultStatus, 1), false);
        if (umsMemberLevel == null) {
            throw new BizException("请设置默认会员");
        }
        //4. 注册用户
        UmsMember member = new UmsMember();
        member.setUsername(username);
        member.setPassword(BCrypt.hashpw(password));
        member.setEmail(email);
        member.setMemberLevelId(umsMemberLevel.getId());
        member.setStatus(1);
        super.save(member);
    }

    private boolean verifyAuthCode(String email, String authCode) {
        String code = memberCacheService.getAuthCode(email);
        if (!StringUtils.hasText(code)) {
            return false;
        }
        return code.equals(authCode);
    }

    @Override
    public SaTokenInfo login(String username, String password) {
        // 1. 检查用户是否存在
        UmsMember umsMember = super.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getUsername, username),false);
        if (umsMember == null) {
            throw new BizException("用户不存在");
        }
        // 2. 验证账号状态&验证密码
        if (!umsMember.getStatus().equals(1)) {
            throw new BizException("该账号已被禁用");
        }
        if (!BCrypt.checkpw(password, umsMember.getPassword())) {
            throw new BizException("密码错误");
        }
        // 3. 执行登录逻辑
        StpMemberUtil.login(umsMember.getId());
        UmsMemberResponseDTO userDTO = new UmsMemberResponseDTO();
        userDTO.setUsername(umsMember.getUsername());
        userDTO.setId(umsMember.getId());
        // 将用户信息存到Session中
        StpMemberUtil.getSession().set(AuthConstant.STP_ADMIN_INFO,userDTO);
        // 获取当前登录用户token
        return StpMemberUtil.getTokenInfo();
    }

    @Override
    public UmsMemberResponseDTO getCurrentMember() {
        // 直接从session里面取
        return (UmsMemberResponseDTO) StpMemberUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
    }

    @Override
    public void logout() {
        StpMemberUtil.logout();
    }

    @Override
    public void generateAuthCode(String email) {
        if (!memberCacheService.canSendAuthCode(email)) {
            throw new BizException("请勿频繁获取验证码");
        }
        // 生成四位验证码
        String auth = String.format("%04d", (int)(Math.random() * 10000));
        // TODO 后续接入邮箱服务
        // 先打印
        log.info("验证码为：{}",auth);
        // 存入 Redis
        memberCacheService.setAuthCode(email, auth);
    }


    @Override
    public void updatePassword(String email, String password, String authCode) {
        // 1. 检查验证码
        if (!verifyAuthCode(email,authCode)) {
            throw new BizException("验证码错误");
        }
        // 2. 检查该邮箱是否存在（兜底，防止恶意灌注数据）
        UmsMember member = super.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getEmail, email), false);
        if (member == null) {
            throw new BizException("邮箱不存在");
        }
        // 3. 修改密码
        member.setPassword(BCrypt.hashpw(password));
        super.updateById(member);
    }
}
