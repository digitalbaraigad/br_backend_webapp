package com.baraigad.audit.config;

import com.baraigad.audit.service.AdminAuditService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component @RequiredArgsConstructor
public class AdminAuditInterceptor implements HandlerInterceptor {
    private final AdminAuditService auditService;
    @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        auditService.recordRequest(request, response.getStatus());
    }
}
