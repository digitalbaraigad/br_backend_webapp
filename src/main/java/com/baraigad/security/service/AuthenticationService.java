package com.baraigad.security.service;

import com.baraigad.security.dto.LoginRequestDto;
import com.baraigad.security.dto.LoginResponseDto;

public interface AuthenticationService {

    LoginResponseDto login(
            LoginRequestDto request);
}