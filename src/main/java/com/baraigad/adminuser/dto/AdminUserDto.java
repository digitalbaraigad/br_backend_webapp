package com.baraigad.adminuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminUserDto {
   // private int userRoleId;
    private String userId;
    private String userFName;
    private String userMName;
    private String userLName;
    private String userEmail;
    private long userMobile;
    private String userPassword;
    private String userStatus;
    private String userRole;
    private String userType;
    private String userCity;
    private String userState;
    private String userCountry;
    private String userPostalCode;
    private Date lastLoginDate;
    private boolean delFlg;

}
