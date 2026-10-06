package com.baraigad.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetConfirmDto {
    @NotBlank(message = "Email address is required")
    @Email(message = "Enter a valid email address")
    private String userEmail;
    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "\\d{6}", message = "OTP must contain 6 digits")
    private String otp;
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must contain at least 8 characters")
    private String newPassword;
}
