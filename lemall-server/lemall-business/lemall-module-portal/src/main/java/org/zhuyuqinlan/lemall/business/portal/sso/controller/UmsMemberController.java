package org.zhuyuqinlan.lemall.business.portal.sso.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.UmsMemberDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员登录注册管理", description = "UmsMemberController")
@RequestMapping("${lemall.server.prefix.portal}/sso")
public class UmsMemberController {
    private final UmsMemberService memberService;
    @Value("${sa-token.token-prefix}")
    private String tokenHead;

    @Operation(summary = "会员注册")
    @PostMapping("/register")
    public Result<?> register(@NotBlank @RequestParam String username,
                              @NotBlank @RequestParam String password,
                              @NotBlank @RequestParam String email,
                              @NotBlank @RequestParam String authCode) {
        memberService.register(username, password, email, authCode);
        return Result.success();
    }

    @Operation(summary = "会员登录")
    @PostMapping("/login")
    public Result<?> login(@NotBlank @RequestParam String username,
                              @NotBlank @RequestParam String password) {
        SaTokenInfo saTokenInfo  = memberService.login(username, password);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", saTokenInfo.getTokenValue() );
        tokenMap.put("tokenHead", tokenHead+" ");
        return Result.success(tokenMap);
    }

    @Operation(summary = "获取会员信息")
    @GetMapping("/info")
    public Result<?> info() {
        UmsMemberDTO member = memberService.getCurrentMember();
        return Result.success(member);
    }

    @Operation(summary = "登出功能")
    @PostMapping("/logout")
    public Result<?> logout() {
        memberService.logout();
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PostMapping("/updatePassword")
    public Result<?> updatePassword(@RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String authCode) {
        memberService.updatePassword(email,password,authCode);
        return Result.success();
    }
}
