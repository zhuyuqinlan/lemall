package org.zhuyuqinlan.lemall.common.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Json序列化配置
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // Long -> String，避免前端精度丢失
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

            // 注册模块
            builder.modules(simpleModule);
        };
    }
}
