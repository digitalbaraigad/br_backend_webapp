package com.baraigad.security.service;

import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.entity.AdminRole;
import com.baraigad.adminuser.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final AdminUserRepository
            repository;

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        AdminUser user =
                repository
                        .findByUserEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found"));

        if (user.isDelFlg()) {

            throw new UsernameNotFoundException(
                    "User account inactive");
        }

        return User.builder()
                .username(user.getUserEmail())
                .password(user.getUserPassword())
                .roles(AdminRole.normalize(user.getUserRole()))
                .build();
    }
}
