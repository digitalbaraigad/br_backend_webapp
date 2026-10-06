package com.baraigad.adminuser.mapper;

import com.baraigad.adminuser.dto.AdminUserDto;
import com.baraigad.adminuser.entity.AdminUser;

public class AdminUserMapper {

    public static AdminUserDto mapToAdminUserDto(AdminUser adminUser) {
        //AdminUserDto adminUserDto = new AdminUserDto();
        return new AdminUserDto(
               // adminUser.getUserRoleId(),
                adminUser.getUserId(),
                adminUser.getUserFName(),
                adminUser.getUserMName(),
                adminUser.getUserLName(),
                adminUser.getUserEmail(),
                adminUser.getUserMobile(),
                adminUser.getUserPassword(),
                adminUser.getUserStatus(),
                adminUser.getUserRole(),
                adminUser.getUserType(),
                adminUser.getUserCity(),
                adminUser.getUserCountry(),
                adminUser.getUserState(),
                adminUser.getUserPostalCode(),
                adminUser.getLastLoginDate(),
                adminUser.isDelFlg()
        );
    }

    public static AdminUser mapToAdminUser(AdminUserDto dto) {

        AdminUser adminUser = new AdminUser();

        // DO NOT SET userRoleId

        adminUser.setUserId(dto.getUserId());
        adminUser.setUserFName(dto.getUserFName());
        adminUser.setUserMName(dto.getUserMName());
        adminUser.setUserLName(dto.getUserLName());
        adminUser.setUserEmail(dto.getUserEmail());
        adminUser.setUserMobile(dto.getUserMobile());
        adminUser.setUserPassword(dto.getUserPassword());
        adminUser.setUserStatus(dto.getUserStatus());
        adminUser.setUserRole(dto.getUserRole());
        adminUser.setUserType(dto.getUserType());
        adminUser.setUserCity(dto.getUserCity());
        adminUser.setUserCountry(dto.getUserCountry());
        adminUser.setUserState(dto.getUserState());
        adminUser.setUserPostalCode(dto.getUserPostalCode());
        adminUser.setLastLoginDate(dto.getLastLoginDate());
        adminUser.setDelFlg(dto.isDelFlg());

        return adminUser;
    }
}

