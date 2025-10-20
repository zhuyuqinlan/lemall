package org.zhuyuqinlan.lemall.common.web.captcha;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhuyuqinlan.lemall.common.captcha.constant.CaptchaConstant;
import org.zhuyuqinlan.lemall.common.captcha.service.CaptchaService;
import org.zhuyuqinlan.lemall.common.response.BizException;
import org.zhuyuqinlan.lemall.common.response.Result;

@Slf4j
@Validated
@RestController
@Tag(name = "邮箱验证码", description = "CaptchaController")
@RequestMapping("${lemall.server.prefix.common}/captcha/email")
public class CaptchaEmailController {
    private final CaptchaService captchaService;

    public CaptchaEmailController(@Qualifier("captchaEmailServiceImpl") CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/getAuthCode")
    public Result<?> getAuthCode(@RequestParam String email) {
        if (!captchaService.canSendAuthCode(email, CaptchaConstant.EMAIL)) {
            throw new BizException("请勿频繁获取验证码");
        }
        String code = captchaService.setAuthCode(email, CaptchaConstant.EMAIL, 300, 60);
        log.info("验证码为：{}", code);
        return Result.success();
    }
}
