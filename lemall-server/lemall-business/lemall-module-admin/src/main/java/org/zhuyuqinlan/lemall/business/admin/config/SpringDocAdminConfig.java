package org.zhuyuqinlan.lemall.business.admin.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该业务模块的doc配置类
 */
@Configuration
public class SpringDocAdminConfig {

    @Value("${lemall.server.prefix.admin}")
    private String adminPrefix;

    @Bean
    public GroupedOpenApi adminGroupApi() {
        return GroupedOpenApi.builder()
                .group("后台管理接口")
                .pathsToMatch(adminPrefix + "/**")
                .build();
    }
}
