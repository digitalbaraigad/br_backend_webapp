package com.baraigad.security.filter;

import com.baraigad.adminmenu.service.AdminMenuAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AdminMenuAccessFilter extends OncePerRequestFilter {
    private final AdminMenuAccessService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !service.isRequestAllowed(authentication.getName(), request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You do not have access to this Admin Portal menu.");
            return;
        }
        chain.doFilter(request, response);
    }
}
