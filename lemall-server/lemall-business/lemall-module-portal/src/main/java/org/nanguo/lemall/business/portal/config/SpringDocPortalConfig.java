package org.nanguo.lemall.business.portal.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该业务模块的doc配置类
 */
@Configuration
public class SpringDocPortalConfig {

    @Value("${lemall.server.prefix.portal}")
    private String portalPrefix;

    @Bean
    public GroupedOpenApi portalGroupApi() {
        return GroupedOpenApi.builder()
                .group("用户端接口")
                .pathsToMatch(portalPrefix + "/**")
                .build();
    }
}
