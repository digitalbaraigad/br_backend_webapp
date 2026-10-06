package com.baraigad.adminuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info")
public class AdminUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_role_id")
    private Integer userRoleId;

    @Column(name="user_id",unique = true)
    private String userId;

    @Column(name="user_f_name")
    private String userFName;

    @Column(name="user_m_name")
    private String userMName;
    @Column(name="user_l_name")
    private String userLName;

    @Column(name="user_email",unique = true)
    private String userEmail;

    @Column(name="user_mobile")
    private long userMobile;
    @Column(name="user_password")
    private String userPassword;

    @Column(name="user_status")
    private String userStatus;

    @Column(name="user_role")
    private String userRole;

    @Column(name="user_type")
    private String userType;

    @Column(name="user_city")
    private String userCity;

    @Column(name="user_state")
    private String userState;

    @Column(name="user_country")
    private String userCountry;

    @Column(name="user_postal_code")
    private String userPostalCode;

    @Column(name="user_login_date")
    private Date lastLoginDate;

    @Column(name="del_flg")
    private boolean delFlg;

}