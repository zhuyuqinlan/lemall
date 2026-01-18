package org.zhuyuqinlan.lemall.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "springdoc.info")
public class SpringDocProperties {
    private String title;
    private String description;
    private String version;
    private Contact contact;
    private License license;
    private String termsOfService;
    private String summary;
    private String securitySchemeName;

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
