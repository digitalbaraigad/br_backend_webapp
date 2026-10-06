package com.baraigad.audit.controller;

import com.baraigad.audit.dto.*;
import com.baraigad.audit.service.AdminAuditService;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;
import com.baraigad.common.response.PagedResponseDto;

/** Append-only audit API. No update or delete endpoints are intentionally exposed. */
@RestController @RequestMapping("/api/v1/admin-audit") @RequiredArgsConstructor
public class AdminAuditController {
    private final AdminAuditService service;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<AdminAuditLogDto>>> recent(
            @RequestParam(defaultValue = "2") int days,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String userId) {
        return ResponseUtil.success("Admin audit activity fetched successfully",
                fromDate != null || toDate != null
                        ? service.byDateRange(fromDate, toDate, pageNo, pageSize, search, email, userId)
                        : service.recent(days, pageNo, pageSize, search));
    }

    @PostMapping("/client-event")
    public ResponseEntity<ApiResponse<Object>> clientEvent(@Valid @RequestBody AdminAuditEventRequest request, HttpServletRequest servletRequest) {
        service.recordClientEvent(request, servletRequest);
        return ResponseUtil.success("Admin activity recorded", null);
    }
}
