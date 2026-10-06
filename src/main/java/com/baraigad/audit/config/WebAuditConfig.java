package com.baraigad.audit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration @RequiredArgsConstructor
public class WebAuditConfig implements WebMvcConfigurer {
    private final AdminAuditInterceptor adminAuditInterceptor;
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuditInterceptor).addPathPatterns("/api/**");
    }
}
