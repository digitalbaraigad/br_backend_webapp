package com.baraigad.security.controller;

import com.baraigad.common.Constants;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.audit.service.AdminAuditService;
import com.baraigad.security.dto.LoginRequestDto;
import com.baraigad.security.dto.LoginResponseDto;
import com.baraigad.security.dto.PasswordResetConfirmDto;
import com.baraigad.security.dto.PasswordResetRequestDto;
import com.baraigad.security.dto.OtpVerifyDto;
import com.baraigad.security.service.AuthenticationService;
import com.baraigad.security.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(Constants.AUTH_REQ_MAPPING)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService
            authenticationService;
    private final PasswordResetService passwordResetService;
    private final AdminAuditService adminAuditService;

    @PostMapping(Constants.AUTH_POST_MAPPING)
    public ResponseEntity<
            ApiResponse<LoginResponseDto>>
    login(
            @RequestBody LoginRequestDto request, HttpServletRequest servletRequest) {

        LoginResponseDto response = authenticationService.login(request);
        adminAuditService.recordLogin(request.getUserEmail(), servletRequest);

        return ResponseUtil.success(
                Constants.LOGIN_SUCCESS,
                response);
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiResponse<Object>> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDto request) {
        passwordResetService.requestOtp(request.getUserEmail());
        return ResponseUtil.success("If an administrator account matches this email, an OTP has been sent.", null);
    }

    @PostMapping("/password-reset/verify")
    public ResponseEntity<ApiResponse<Object>> verifyPasswordReset(@Valid @RequestBody OtpVerifyDto request) {
        passwordResetService.verifyOtp(request.getUserEmail(), request.getOtp());
        return ResponseUtil.success("OTP verified. You can now create a new password.", null);
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<ApiResponse<Object>> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmDto request) {
        passwordResetService.resetPassword(request.getUserEmail(), request.getOtp(), request.getNewPassword());
        return ResponseUtil.success("Password reset successfully. Please sign in with your new password.", null);
    }
}
