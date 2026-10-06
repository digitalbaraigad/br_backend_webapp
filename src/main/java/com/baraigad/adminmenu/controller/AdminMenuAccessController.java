package com.baraigad.adminmenu.controller;

import com.baraigad.adminmenu.dto.AdminMenuAccessDto;
import com.baraigad.adminmenu.service.AdminMenuAccessService;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin-menu-access")
@RequiredArgsConstructor
public class AdminMenuAccessController {
    private final AdminMenuAccessService service;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<AdminMenuAccessDto>> myAccess(Authentication authentication) {
        return ResponseUtil.success("Admin menu access fetched successfully", service.getForEmail(authentication.getName()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminMenuAccessDto>> getForUser(@PathVariable String userId) {
        return ResponseUtil.success("Admin menu mapping fetched successfully", service.getForUser(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminMenuAccessDto>> updateForUser(
            @PathVariable String userId, @RequestBody AdminMenuAccessDto request) {
        return ResponseUtil.success("Admin menu mapping updated successfully",
                service.replaceForAdmin(userId, request.getMenuKeys()));
    }
}
