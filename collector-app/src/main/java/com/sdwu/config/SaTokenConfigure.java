package com.sdwu.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.types.common.StpUserUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
//        registry.addInterceptor(new SaInterceptor(handle -> {
//
//            SaRouter.match("/**").check(r->StpUtil.checkLogin());
//            SaRouter.match("/cms/**").check(r-> StpUserUtil.checkLogin());
//        })).addPathPatterns("/**").excludePathPatterns("/login")
//                .excludePathPatterns( "/captchaImage/**");
//    }
}
