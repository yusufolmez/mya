package com.olmez.mya.security.springsecurity.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;

/**
 * In order for Spring Boot to perform a security check of incoming requests,
 * this class allows adding our own securityInterceptor to SpringBoot's
 * interceptor.
 */
@Configuration
@AllArgsConstructor
public class WebInterceptorConfig implements WebMvcConfigurer {

    private final SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**");
    }
}
