package com.baraigad.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDto {
    @NotBlank(message = "Email address is required")
    @Email(message = "Enter a valid email address")
    private String userEmail;
}
