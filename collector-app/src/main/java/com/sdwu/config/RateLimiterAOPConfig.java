package com.sdwu.config;


import com.sdwu.types.aop.RateLimiterAOP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RateLimiterAOPConfig {

    @Bean
    public RateLimiterAOP rateLimiter(){
        return new RateLimiterAOP();
    }

}
