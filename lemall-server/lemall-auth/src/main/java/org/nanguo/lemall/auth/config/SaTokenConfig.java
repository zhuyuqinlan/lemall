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
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.auth.constant.AuthConstant;
import org.nanguo.lemall.auth.util.StpMemberUtil;
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

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setExcludeList(ignoreUrlsConfig.getUrls())
                .setAuth(obj -> {
                    String requestPath = SaHolder.getRequest().getRequestPath();
                    PathContainer pathToMatch = PathContainer.parsePath(requestPath);

                    // 1. 门户端：只检查登录
                    SaRouter.match("/" + portalPrefix + "/**", r -> {
                        StpMemberUtil.checkLogin();
                    }).stop();

                    // 2. 管理端：先登录，再权限校验
                    SaRouter.match("/" + adminPrefix + "/**", r -> {
                        StpUtil.checkLogin();
                        // Redis 权限匹配
                        Map<Object, Object> pathResourceMap = redisTemplate.opsForHash()
                                .entries(AuthConstant.PATH_RESOURCE_MAP);

                        List<String> needPermissionList = new ArrayList<>();
                        for (Map.Entry<Object, Object> entry : pathResourceMap.entrySet()) {
                            String patternStr = (String) entry.getKey();
                            PathPattern pattern = patternCache.computeIfAbsent(patternStr, patternParser::parse);
                            if (pattern.matches(pathToMatch)) {
                                needPermissionList.add((String) entry.getValue());
                            }
                        }

                        // 校验权限
                        StpUtil.checkPermissionOr(Convert.toStrArray(needPermissionList));
                    }).stop();


                    // 3.兜底：除了白名单外，所有未匹配到的路径都拒绝访问
                    SaRouter.match("/**", r -> {
                        throw new NotPermissionException("未授权访问");
                    });
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
                        result = Result.fail(e.getMessage());
                    } else if (e instanceof NotPermissionException) {
                        result = Result.fail(e.getMessage());
                    } else {
                        result = Result.fail(e.getMessage());
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

