package org.zhuyuqinlan.lemall.auth.config;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.common.constant.AuthConstant;
import org.zhuyuqinlan.lemall.common.dto.AdminUserDto;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.zhuyuqinlan.lemall.common.service.RedisService;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Sa-Token 配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IgnoreUrlsConfig.class)
public class SaTokenConfig {

    private final RedisService redisService;

    @Value("${lemall.server.prefix.portal}")
    private String portalPrefix;

    @Value("${lemall.server.prefix.admin}")
    private String adminPrefix;

    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;

    @Value("${redis.key.auth.pathResourceMap}")
    private String PATH_RESOURCE_MAP;

    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final PathPatternParser patternParser = PathPatternParser.defaultInstance; // 路径解析器
    private final ConcurrentMap<String, PathPattern> patternCache = new ConcurrentHashMap<>(); // 缓存路径模式

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setExcludeList(ignoreUrlsConfig.getUrls())
                .setAuth(obj -> {

                    // ---------- 1. 门户端鉴权 ----------
                    SaRouter.match(portalPrefix + "/**", r -> StpMemberUtil.checkLogin()).stop();

                    // ---------- 2. 管理端鉴权 ----------
                    SaRouter.match(adminPrefix + "/**", r -> {
                        // 先判断登录
                        StpUtil.checkLogin();

                        // 从 Redis 拿路径资源映射
                        Map<Object, Object> pathResourceMap = redisService.hGetAll(REDIS_PREFIX + ":" + PATH_RESOURCE_MAP);
                        String requestPath = SaHolder.getRequest().getRequestPath();
                        PathContainer pathToMatch = PathContainer.parsePath(requestPath);

                        // 找出匹配当前请求的资源列表
                        List<String> needResourceList = new ArrayList<>();
                        for (Map.Entry<Object, Object> entry : pathResourceMap.entrySet()) {
                            String patternStr = (String) entry.getKey();
                            PathPattern pattern = patternCache.computeIfAbsent(patternStr, patternParser::parse);
                            if (pattern.matches(pathToMatch)) {
                                needResourceList.add((String) entry.getValue());
                            }
                        }

                        // 如果没有配置资源要求，则默认放行
                        if (needResourceList.isEmpty()) {
                            return;
                        }

                        // 获取当前登录用户的资源列表
                        AdminUserDto user = (AdminUserDto) StpUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
                        List<String> resourceList = user.getResourceList();

                        // 路径匹配器（支持通配符）
                        AntPathMatcher matcher = new AntPathMatcher();

                        // 检查是否拥有访问权限（任意一个匹配即可）
                        boolean hasPermission = needResourceList.stream().anyMatch(need ->
                                resourceList.stream().anyMatch(r1 -> matcher.match(r1, need))
                        );

                        if (!hasPermission) {
                            throw new NotPermissionException("无权限访问资源: " + requestPath);
                        }
                    }).stop();

                    // ---------- 3. 全局兜底 ----------
                    SaRouter.match("/**", r -> {
                        boolean hasMemberToken = StpMemberUtil.isLogin();
                        boolean hasAdminToken = StpUtil.isLogin();
                        if (!hasMemberToken && !hasAdminToken) {
                            throw new NotLoginException("请求未携带有效 token，访问被拒绝", null, null);
                        }
                    }).stop();
                })
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setHeader("Access-Control-Max-Age", "3600");

                    // 如果是预检请求，直接放行
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
                        result = Result.fail(401, e.getMessage());
                    } else if (e instanceof NotPermissionException) {
                        result = Result.fail(403, e.getMessage());
                    } else {
                        result = Result.fail(500, e.getMessage());
                    }
                    return JSONUtil.toJsonStr(result);
                });
    }

    // Sa-Token 整合 JWT (Simple 模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 为 StpMemberUtil 注入 StpLogicJwt 实现
     */
    @Autowired
    public void setMemberStpLogic() {
        StpMemberUtil.setStpLogic(new StpLogicJwtForSimple(StpMemberUtil.TYPE));
    }
}
