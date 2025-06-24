//package com.example.boardHub.global.config;
//
//import com.example.boardHub.global.interceptor.LoginCheckInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
//
//    private final LoginCheckInterceptor loginCheckInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginCheckInterceptor)
//                .addPathPatterns("/board/**") // 로그인 필요한 경로
//                .excludePathPatterns("/user/**", "/h2-console/**"); // 로그인 필요 없는 경로
//    }
//}
