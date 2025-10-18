package org.zhuyuqinlan.lemall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = "org.zhuyuqinlan.lemall.business",
        nameGenerator = BusinessModuleConfig.BusinessBeanNameGenerator.class
)
@MapperScan(
        basePackages = "org.zhuyuqinlan.lemall.business.**.dao",
        nameGenerator = BusinessModuleConfig.BusinessMapperBeanNameGenerator.class
)
public class BusinessModuleConfig {

    // 普通 Spring Bean 前缀
    public static class BusinessBeanNameGenerator implements BeanNameGenerator {
        private final AnnotationBeanNameGenerator defaultGenerator = new AnnotationBeanNameGenerator();

        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            String className = definition.getBeanClassName();
            if (className != null && className.startsWith("org.zhuyuqinlan.lemall.business")) {
                String moduleName = getModuleName(className);
                String simpleName = className.substring(className.lastIndexOf('.') + 1);
                return moduleName + "_" + Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            }
            return defaultGenerator.generateBeanName(definition, registry);
        }
    }

    // Mapper Bean 前缀
    public static class BusinessMapperBeanNameGenerator implements BeanNameGenerator {
        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            String className = definition.getBeanClassName();
            if (className != null && className.startsWith("org.zhuyuqinlan.lemall.business")) {
                String moduleName = getModuleName(className);
                String simpleName = className.substring(className.lastIndexOf('.') + 1);
                return moduleName + "_" + Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            }
            return className; // fallback
        }
    }

    // 公共方法：获取 business 子模块名
    private static String getModuleName(String className) {
        String[] parts = className.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            if ("business".equals(parts[i]) && parts.length > i + 1) {
                return parts[i + 1];
            }
        }
        return "business";
    }
}

