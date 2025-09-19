package org.zhuyuqinlan.lemall.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
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
                                        .type(SecurityScheme.Type.valueOf(properties.getSecuritySchemeType()))
                                        .scheme(properties.getSecuritySchemeScheme())
                                        .bearerFormat(properties.getSecuritySchemeBearerFormat())));
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
}


