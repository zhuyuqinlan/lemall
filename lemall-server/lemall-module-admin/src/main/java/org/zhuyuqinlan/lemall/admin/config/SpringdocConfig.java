package org.zhuyuqinlan.lemall.admin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共doc配置类
 */
@Configuration
@EnableConfigurationProperties(SpringDocProperties.class)
public class SpringdocConfig {

    private final SpringDocProperties properties;

    @Value("${lemall.server.prefix.admin}")
    private String adminPrefix;

    public SpringdocConfig(SpringDocProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .components(new Components()
                        .addSecuritySchemes(properties.getSecuritySchemeName(),
                                new SecurityScheme()
                                        .name(properties.getSecuritySchemeName())
                                        .type(SecurityScheme.Type.valueOf("HTTP"))
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    private Info getApiInfo() {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact()
                        .name(properties.getContact().getName())
                        .url(properties.getContact().getUrl())
                        .email(properties.getContact().getEmail()))
                .license(new License()
                        .name(properties.getLicense().getName())
                        .url(properties.getLicense().getUrl()))
                .termsOfService(properties.getTermsOfService())
                .summary(properties.getSummary());
    }

    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((s, pathItem) -> {
                    pathItem.readOperations().forEach(operation -> {
                        operation.addSecurityItem(new SecurityRequirement().addList(properties.getSecuritySchemeName()));
                    });
                });
            }
        };
    }

    @Bean
    public GroupedOpenApi adminGroupApi() {
        return GroupedOpenApi.builder()
                .group("后台管理接口")
                .pathsToMatch(adminPrefix + "/**")
                .build();
    }
}
