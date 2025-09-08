package org.nanguo.lemall.auth.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.common.constant.AuthConstant;
import org.nanguo.lemall.auth.util.StpMemberUtil;
import org.nanguo.lemall.common.dto.AdminUserDto;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.nanguo.lemall.auth.service.UmsAdminCacheService;

/**
 * sa-token配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IgnoreUrlsConfig.class)
public class SaTokenConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${lemall.server.prefix.portal}")
    private String portalPrefix;
    @Value("${lemall.server.prefix.admin}")
    private String adminPrefix;

    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final PathPatternParser patternParser = PathPatternParser.defaultInstance; // 路径解析器
    private final ConcurrentMap<String, PathPattern> patternCache = new ConcurrentHashMap<>(); // 缓存解析后的 PathPattern
    private final UmsAdminCacheService umsAdminCacheService;

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setExcludeList(ignoreUrlsConfig.getUrls())
                .setAuth(obj -> {
                    String requestPath = SaHolder.getRequest().getRequestPath();
                    PathContainer pathToMatch = PathContainer.parsePath(requestPath);

                    // 1. 门户端：只检查登录
                    SaRouter.match(portalPrefix + "/**", r -> {
                        StpMemberUtil.checkLogin();
                    }).stop();

                    // 2. 管理端：先登录，再权限校验
                    SaRouter.match(adminPrefix + "/**", r -> {
                        StpUtil.checkLogin();
                        // Redis 权限匹配
                        Map<Object, Object> pathResourceMap = redisTemplate.opsForHash()
                                .entries(AuthConstant.PATH_RESOURCE_MAP);

                        List<String> needResourceList = new ArrayList<>();
                        for (Map.Entry<Object, Object> entry : pathResourceMap.entrySet()) {
                            String patternStr = (String) entry.getKey();
                            PathPattern pattern = patternCache.computeIfAbsent(patternStr, patternParser::parse);
                            if (pattern.matches(pathToMatch)) {
                                needResourceList.add((String) entry.getValue());
                            }
                        }

                        // 校验资源列
                        AdminUserDto user = umsAdminCacheService.getAdmin((Long) StpUtil.getLoginId());
                        if (user == null) {
                            StpUtil.logout();
                            throw new NotLoginException("未登录",AuthConstant.STP_ADMIN_LOGIN_TYPE,AuthConstant.STP_ADMIN_LOGIN_TYPE);
                        }
                        List<String> resourceList = user.getResourceList();
                        for (String resource : needResourceList) {
                            if (!resourceList.contains(resource)) {
                                throw new NotPermissionException("无权限访问资源: " + resource);
                            }
                        }

                        // 如果needResourceList中没有配置相关路径，那就不需要鉴权，直接访问...

                    }).stop();
                })
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setHeader("Access-Control-Max-Age", "3600");

                    // 如果是预检请求，则立即返回
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> {})
                            .back();
                })
                .setError(e -> {
                    HttpServletResponse response = (HttpServletResponse) SaHolder.getResponse().getSource();
                    response.setHeader("Content-Type", "application/json; charset=utf-8");
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Cache-Control", "no-cache");

                    Result<?> result;
                    if (e instanceof NotLoginException) {
                        result = Result.fail(401,e.getMessage());
                    } else if (e instanceof NotPermissionException) {
                        result = Result.fail(403,e.getMessage());
                    } else {
                        result = Result.fail(500,e.getMessage());
                    }
                    return JSONUtil.toJsonStr(result);
                });
    }

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}

