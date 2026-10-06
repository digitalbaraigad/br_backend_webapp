package com.baraigad.analytics.controller;

import com.baraigad.analytics.dto.WebsiteVisitMetricsDto;
import com.baraigad.analytics.dto.WebsiteVisitRequest;
import com.baraigad.analytics.service.WebsiteVisitService;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/website-visits")
@RequiredArgsConstructor
public class WebsiteVisitController {
    private final WebsiteVisitService websiteVisitService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody WebsiteVisitRequest request) {
        websiteVisitService.registerVisit(request.getVisitorId());
        return ResponseUtil.created("Website visit registered", "Success");
    }

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<WebsiteVisitMetricsDto>> metrics() {
        return ResponseUtil.success("Website visit metrics fetched successfully", websiteVisitService.getMetrics());
    }
}
