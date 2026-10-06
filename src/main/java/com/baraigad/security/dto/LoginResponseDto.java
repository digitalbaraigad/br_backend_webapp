package com.baraigad.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponseDto {

    private String token;

    private String userEmail;

    private String userRole;

    private String userName;
}