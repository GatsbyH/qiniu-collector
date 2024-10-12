package com.sdwu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径生效
            .allowedOrigins("*") // 允许所有源地址
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
            .allowedHeaders("*"); // 允许的请求头
    }
}
