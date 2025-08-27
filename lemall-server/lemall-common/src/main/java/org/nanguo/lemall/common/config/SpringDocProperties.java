package org.nanguo.lemall.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "springdoc.info")
@Data
public class SpringDocProperties {
    private String title;
    private String description;
    private String version;
    private Contact contact;
    private License license;
    private String termsOfService;
    private String summary;
    private final String securitySchemeName = "Authorization";
    private final String securitySchemeType = "HTTP";
    private final String securitySchemeScheme = "bearer";
    private final String securitySchemeBearerFormat = "JWT";

    // getters and setters

    @Data
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Data
    public static class License {
        private String name;
        private String url;
    }
}
