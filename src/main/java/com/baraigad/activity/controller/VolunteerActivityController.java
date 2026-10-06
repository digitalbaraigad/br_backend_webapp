package com.baraigad.activity.controller;

import com.baraigad.activity.dto.ActivityRegistrationRequestDto;
import com.baraigad.activity.service.VolunteerActivityService;
import com.baraigad.common.Constants;
import com.baraigad.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.VOLUNTEER_ACTIVITY)
@RequiredArgsConstructor
public class VolunteerActivityController {

    private final VolunteerActivityService service;

    @PostMapping("/register")
    public Object register(
            @Valid @RequestBody ActivityRegistrationRequestDto request) {

        service.registerVolunteer(request);

        return ResponseUtil.success(
                "Volunteer registered successfully",
                null);
    }

    @GetMapping("/history/{mobileNumber}")
    public Object getHistory(
            @PathVariable String mobileNumber) {

        return ResponseUtil.success(
                "Volunteer history fetched successfully",
                service.getVolunteerHistory(
                        mobileNumber));
    }
}
