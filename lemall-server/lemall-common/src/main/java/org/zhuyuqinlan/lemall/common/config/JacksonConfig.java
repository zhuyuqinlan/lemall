package org.zhuyuqinlan.lemall.common.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Json序列化配置
 */
@Configuration
public class JacksonConfig {

    private static final String DATETIME_PATTERN = "yyyy/MM/dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy/MM/dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // 保留 Long -> String 的配置，避免前端精度丢失
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

            // 设置 java.util.Date（以及 java.sql.*、Calendar 等基于 Date 的类型）的全局格式
            builder.simpleDateFormat(DATETIME_PATTERN);
            // 禁用时间戳输出，确保以字符串形式输出日期
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Java 8 时间类型的格式化（LocalDateTime/LocalDate/LocalTime）
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
            javaTimeModule.addSerializer(LocalDate.class,
                    new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
            javaTimeModule.addSerializer(LocalTime.class,
                    new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_PATTERN)));

            // 注册模块（保留 long->string 的 simpleModule，并加入 JavaTimeModule）
            builder.modules(simpleModule, javaTimeModule);

            // 可选：设置时区，默认使用系统时区；若需要使用 UTC 可改为 TimeZone.getTimeZone("UTC")
            builder.timeZone(TimeZone.getDefault());
        };
    }
}
