package org.zhuyuqinlan.lemall.common.web.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该业务模块的doc配置类
 */
@Configuration
public class SpringDocLemallCommonConfig {
    @Bean
    public GroupedOpenApi lemallCommonWebApi() {
        return GroupedOpenApi.builder()
                .group("公共接口")
                .pathsToMatch("/lemall/common/**")
                .build();
    }
}
