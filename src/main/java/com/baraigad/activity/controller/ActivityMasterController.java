package com.baraigad.activity.controller;

import com.baraigad.activity.dto.ActivityRequestDto;
import com.baraigad.activity.service.ActivityMasterService;
import com.baraigad.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class ActivityMasterController {

    private final ActivityMasterService activityService;

    @PostMapping
    public Object createActivity(
            @RequestBody ActivityRequestDto request) {

        return ResponseUtil.success(
                "Activity created successfully",
                activityService.createActivity(
                        request));
    }

    @GetMapping
    public Object getAllActivities() {

        return ResponseUtil.success(
                "Activities fetched successfully",
                activityService.getAllActivities());
    }
}