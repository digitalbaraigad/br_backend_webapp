package com.baraigad.security.service;

import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.entity.AdminRole;
import com.baraigad.adminuser.repository.AdminUserRepository;
import com.baraigad.common.exception.InvalidCredentialsException;
import com.baraigad.common.exception.UserInactiveException;
import com.baraigad.security.dto.LoginRequestDto;
import com.baraigad.security.dto.LoginResponseDto;
import com.baraigad.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl
        implements AuthenticationService {

    private final AdminUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public LoginResponseDto login(
            LoginRequestDto request) {

        System.out.println("Email Received = "
                + request.getUserEmail());

        AdminUser user1 = repository
                .findByUserEmail(request.getUserEmail())
                .orElse(null);

        System.out.println("User Found = "
                + (user1 != null));

        if (user1 == null) {
            throw new InvalidCredentialsException();
        }

        if (user1.isDelFlg()) {

            throw new UserInactiveException(
                    "Your account has been deactivated. Please contact administrator.");
        }
        AdminUser user =
                repository
                        .findByUserEmailAndDelFlgFalse(
                                request.getUserEmail())
                        .orElseThrow(() ->
                        new InvalidCredentialsException());


        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getUserPassword())) {

            throw new InvalidCredentialsException();
        }

        String token =
                jwtService.generateToken(
                        user.getUserEmail());

        return LoginResponseDto.builder()
                .token(token)
                .userEmail(user.getUserEmail())
                .userRole(AdminRole.normalize(user.getUserRole()))
                .userName(user.getUserFName()
                        + " "
                        + user.getUserLName())
                .build();
    }
}
