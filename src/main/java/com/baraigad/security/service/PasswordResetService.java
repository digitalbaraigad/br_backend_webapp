package com.baraigad.security.service;

public interface PasswordResetService {
    void requestOtp(String email);
    void verifyOtp(String email, String otp);
    void resetPassword(String email, String otp, String newPassword);
}
