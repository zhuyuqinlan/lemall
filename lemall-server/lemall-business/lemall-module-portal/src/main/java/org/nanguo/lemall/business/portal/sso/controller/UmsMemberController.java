//package org.nanguo.lemall.business.portal.sso.controller;
//
//import cn.dev33.satoken.stp.SaTokenInfo;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Validated
//@RestController
//@RequiredArgsConstructor
//@Tag(name = "会员登录注册管理", description = "UmsMemberController")
//@RequestMapping("${lemall.server.prefix.portal}/sso")
//public class UmsMemberController {
//    private final UmsMemberService memberService;
//    @Value("${sa-token.token-prefix}")
//    private String tokenHead;
//
//    @Operation(summary = "会员注册")
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult<?> register(@RequestParam String username,
//                                 @RequestParam String password,
//                                 @RequestParam String telephone,
//                                 @RequestParam String authCode) {
//        memberService.register(username, password, telephone, authCode);
//        return CommonResult.success(null,"注册成功");
//    }
//
//    @Operation(summary = "会员登录")
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult<?> login(@RequestParam String username,
//                              @RequestParam String password) {
//        SaTokenInfo saTokenInfo  = memberService.login(username, password);
//        if (saTokenInfo  == null) {
//            return CommonResult.validateFailed("用户名或密码错误");
//        }
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("token", saTokenInfo.getTokenValue() );
//        tokenMap.put("tokenHead", tokenHead+" ");
//        return CommonResult.success(tokenMap);
//    }
//
//    @Operation(summary = "获取会员信息")
//    @RequestMapping(value = "/info", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<?> info() {
//        UmsMember member = memberService.getCurrentMember();
//        return CommonResult.success(member);
//    }
//
//    @Operation(summary = "登出功能")
//    @RequestMapping(value = "/logout", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult<?> logout() {
//        memberService.logout();
//        return CommonResult.success(null);
//    }
//
//    @Operation(summary = "获取验证码")
//    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<?> getAuthCode(@RequestParam String telephone) {
//        String authCode = memberService.generateAuthCode(telephone);
//        return CommonResult.success(authCode,"获取验证码成功");
//    }
//
//    @Operation(summary = "修改密码")
//    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult<?> updatePassword(@RequestParam String telephone,
//                                 @RequestParam String password,
//                                 @RequestParam String authCode) {
//        memberService.updatePassword(telephone,password,authCode);
//        return CommonResult.success(null,"密码修改成功");
//    }
//}
