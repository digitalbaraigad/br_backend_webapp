package com.baraigad.audit.service;

import com.baraigad.audit.dto.*;
import com.baraigad.audit.entity.AdminAuditLog;
import com.baraigad.audit.repository.AdminAuditLogRepository;
import com.baraigad.adminuser.repository.AdminUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.List;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service @RequiredArgsConstructor
public class AdminAuditService {
    private final AdminAuditLogRepository repository;
    private final AdminUserRepository adminUserRepository;

    public void recordRequest(HttpServletRequest request, int status) {
        String method = request.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method) || "DELETE".equals(method))) return;
        if (request.getRequestURI().startsWith("/api/v1/admin-audit")) return;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) return;
        record(auth.getName(), actionFor(method), moduleFor(request.getRequestURI()), method, request.getRequestURI(),
                "API request completed", ip(request), status);
    }

    public void recordClientEvent(AdminAuditEventRequest request, HttpServletRequest servletRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? "unknown" : auth.getName();
        record(username, safe(request.getAction(), 40), safe(request.getModuleName(), 100), "UI", "/admin", safe(request.getDetails(), 2000), ip(servletRequest), 200);
    }

    public void recordLogin(String username, HttpServletRequest servletRequest) {
        record(username, "LOGIN", "Authentication", "POST", "/api/auth/login", "Admin login successful", ip(servletRequest), 200);
    }

    public List<AdminAuditLogDto> recent(int days) {
        int safeDays = Math.max(1, Math.min(days, 30));
        return repository.findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(LocalDateTime.now().minusDays(safeDays))
                .stream().map(this::map).toList();
    }

    public PagedResponseDto<AdminAuditLogDto> recent(int days, int pageNo, int pageSize, String search) {
        int safeDays = Math.max(1, Math.min(days, 30));
        int safePage = Math.max(1, pageNo);
        int safeSize = Math.max(1, Math.min(pageSize, 100));
        return PaginationUtil.build(repository.searchBetween(
                LocalDateTime.now().minusDays(safeDays), LocalDateTime.now().plusSeconds(1), "", search == null ? "" : search.trim(),
                PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"))), this::map);
    }

    public PagedResponseDto<AdminAuditLogDto> byDateRange(LocalDate fromDate, LocalDate toDate, int pageNo, int pageSize, String search, String email, String userId) {
        if (fromDate == null || toDate == null) throw new IllegalArgumentException("Both From date and To date are required.");
        if (fromDate.isAfter(toDate)) throw new IllegalArgumentException("From date cannot be after To date.");
        if (fromDate.plusDays(366).isBefore(toDate)) throw new IllegalArgumentException("Please select a date range of one year or less.");
        int safePage = Math.max(1, pageNo);
        int safeSize = Math.max(1, Math.min(pageSize, 100));
        String selectedEmail = email == null ? "" : email.trim().toLowerCase();
        if (selectedEmail.isBlank() && userId != null && !userId.isBlank()) {
            selectedEmail = adminUserRepository.findByUserIdAndDelFlgFalse(userId.trim())
                    .map(user -> user.getUserEmail().trim().toLowerCase())
                    .orElse("__no_matching_admin_user__");
        }
        return PaginationUtil.build(repository.searchBetween(
                fromDate.atStartOfDay(), toDate.plusDays(1).atStartOfDay(), selectedEmail, search == null ? "" : search.trim(),
                PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"))), this::map);
    }

    private void record(String user, String action, String module, String method, String path, String details, String ip, int status) {
        AdminAuditLog item = new AdminAuditLog();
        item.setAdminUsername(safe(user, 150)); item.setAction(action); item.setModuleName(module); item.setRequestMethod(method);
        item.setResourcePath(safe(path, 500)); item.setDetails(details); item.setIpAddress(ip); item.setResponseStatus(status); item.setCreatedAt(LocalDateTime.now());
        repository.save(item);
    }
    private AdminAuditLogDto map(AdminAuditLog item) { AdminAuditLogDto dto = new AdminAuditLogDto();
        dto.setAuditId(item.getAuditId()); dto.setAdminUsername(item.getAdminUsername()); dto.setAction(item.getAction()); dto.setModuleName(item.getModuleName()); dto.setRequestMethod(item.getRequestMethod()); dto.setResourcePath(item.getResourcePath()); dto.setDetails(item.getDetails()); dto.setIpAddress(item.getIpAddress()); dto.setResponseStatus(item.getResponseStatus()); dto.setCreatedAt(item.getCreatedAt()); return dto; }
    private String actionFor(String method) { return switch (method) { case "POST" -> "CREATE"; case "PUT", "PATCH" -> "UPDATE"; case "DELETE" -> "DELETE"; default -> "REQUEST"; }; }
    private String moduleFor(String path) { String[] parts = path.split("/"); return parts.length > 3 ? parts[3].replace('-', ' ') : "Admin Portal"; }
    private String ip(HttpServletRequest request) { String forwarded = request.getHeader("X-Forwarded-For"); return safe(forwarded == null ? request.getRemoteAddr() : forwarded.split(",")[0].trim(), 80); }
    private String safe(String value, int max) { if (value == null) return null; return value.length() <= max ? value : value.substring(0, max); }
}
