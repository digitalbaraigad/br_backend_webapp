package com.baraigad.security.service;

import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.repository.AdminUserRepository;
import com.baraigad.common.email.service.EmailService;
import com.baraigad.security.entity.PasswordResetOtp;
import com.baraigad.security.repository.PasswordResetOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private static final int MAX_ATTEMPTS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final AdminUserRepository adminUserRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public void requestOtp(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        AdminUser user = adminUserRepository.findByUserEmailAndDelFlgFalse(normalizedEmail).orElse(null);
        if (user == null) return; // Do not reveal which administrator emails exist.

        PasswordResetOtp previous = otpRepository.findTopByUserEmailAndUsedFalseOrderByIdDesc(normalizedEmail).orElse(null);
        if (previous != null) {
            previous.setUsed(true);
            otpRepository.save(previous);
        }
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setUserEmail(normalizedEmail);
        resetOtp.setOtpHash(passwordEncoder.encode(otp));
        resetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        resetOtp.setAttempts(0);
        resetOtp.setUsed(false);
        otpRepository.save(resetOtp);
        emailService.sendHtmlEmail(normalizedEmail, "Ba Raigad Admin Password Reset OTP",
                "<p>Your Ba Raigad administrator password reset code is:</p><h2>" + otp + "</h2><p>This code expires in 10 minutes. Do not share it with anyone.</p>");
    }

    @Override
    public void verifyOtp(String email, String otp) {
        validateOtp(email, otp);
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        PasswordResetOtp resetOtp = validateOtp(email, otp);
        AdminUser user = adminUserRepository.findByUserEmailAndDelFlgFalse(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Password reset request is no longer valid. Request a new OTP."));
        user.setUserPassword(passwordEncoder.encode(newPassword));
        adminUserRepository.save(user);
        resetOtp.setUsed(true);
        otpRepository.save(resetOtp);
    }

    private PasswordResetOtp validateOtp(String email, String otp) {
        PasswordResetOtp resetOtp = otpRepository.findTopByUserEmailAndUsedFalseOrderByIdDesc(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("OTP is invalid or has expired. Request a new code."));
        if (resetOtp.getExpiresAt().isBefore(LocalDateTime.now()) || resetOtp.getAttempts() >= MAX_ATTEMPTS) {
            resetOtp.setUsed(true);
            otpRepository.save(resetOtp);
            throw new IllegalArgumentException("OTP is invalid or has expired. Request a new code.");
        }
        if (!passwordEncoder.matches(otp, resetOtp.getOtpHash())) {
            resetOtp.setAttempts(resetOtp.getAttempts() + 1);
            otpRepository.save(resetOtp);
            throw new IllegalArgumentException("OTP is incorrect. Please try again.");
        }
        return resetOtp;
    }
}
