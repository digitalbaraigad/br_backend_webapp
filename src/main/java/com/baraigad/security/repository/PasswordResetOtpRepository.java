package com.baraigad.security.repository;

import com.baraigad.security.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findTopByUserEmailAndUsedFalseOrderByIdDesc(String userEmail);
}
