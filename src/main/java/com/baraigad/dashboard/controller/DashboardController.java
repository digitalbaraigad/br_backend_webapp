package com.baraigad.dashboard.controller;

import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.dashboard.dto.DashboardResponseDto;
import com.baraigad.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/homepage")
    public ResponseEntity<ApiResponse<DashboardResponseDto>>
    getHomepageDashboard() {

        return ResponseUtil.success(
                "Dashboard fetched successfully",
                dashboardService.getHomepageDashboard());
    }
}