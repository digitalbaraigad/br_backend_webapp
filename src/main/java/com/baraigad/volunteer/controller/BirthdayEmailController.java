package com.baraigad.volunteer.controller;

import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.volunteer.service.BirthdayEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class BirthdayEmailController {

    private final BirthdayEmailService birthdayEmailService;

    @GetMapping("/birthday/count")
    public ResponseEntity<Integer> getBirthdayCount() {

        return ResponseEntity.ok(
                birthdayEmailService
                        .getTodaysBirthdayCount());
    }

    @PostMapping("/birthday")
    public ResponseEntity<ApiResponse<String>>
    sendBirthdayEmails() {

        birthdayEmailService.sendBirthdayEmails();

        return ResponseUtil.success(
                "Birthday emails sent successfully",
                "SUCCESS");
    }
}