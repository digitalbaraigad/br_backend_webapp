package com.baraigad.security.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String userEmail;

    private String password;
}