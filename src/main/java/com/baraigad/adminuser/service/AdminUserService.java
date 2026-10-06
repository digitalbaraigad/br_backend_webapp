package com.baraigad.adminuser.service;

import com.baraigad.adminuser.dto.AdminUserDto;
import com.baraigad.common.response.PagedResponseDto;

import java.util.List;

public interface AdminUserService {
    AdminUserDto createAdminUser(AdminUserDto adminUserDto);
    AdminUserDto getAdminUserByUserId(String userId);
    PagedResponseDto<AdminUserDto> getAllAdminUser(
            Integer pageNo,
            Integer pageSize);
    AdminUserDto updateAdminUser(String userId,AdminUserDto updatedAdminUser);
    void deleteAdminUser(String userId);


}
