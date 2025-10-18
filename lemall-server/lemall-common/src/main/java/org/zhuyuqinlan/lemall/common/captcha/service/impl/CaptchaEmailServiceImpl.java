package org.zhuyuqinlan.lemall.common.captcha.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.captcha.service.CaptchaRedisService;
import org.zhuyuqinlan.lemall.common.captcha.service.CaptchaService;
// TODO 邮箱验证码
@Service
@RequiredArgsConstructor
public class CaptchaEmailServiceImpl implements CaptchaService {

    private final CaptchaRedisService captchaRedisService;

    @Override
    public String setAuthCode(String target, String type, long expireSeconds, long intervalSeconds) {
        String auth = String.format("%04d", (int)(Math.random() * 10000));
        captchaRedisService.setAuthCode(target,type,auth,expireSeconds,intervalSeconds);
        return auth;
    }

    @Override
    public String getAuthCode(String target, String type) {
        return captchaRedisService.getAuthCode(target,type);
    }

    @Override
    public boolean canSendAuthCode(String target, String type) {
        return captchaRedisService.canSendAuthCode(target,type);
    }
}
