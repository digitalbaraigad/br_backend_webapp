package com.baraigad.adminuser.service;

import com.baraigad.adminuser.dto.AdminUserDto;
import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.entity.AdminRole;
import com.baraigad.adminuser.exception.AdminUserNotFoundException;
import com.baraigad.adminuser.mapper.AdminUserMapper;
import com.baraigad.adminuser.repository.AdminUserRepository;
import com.baraigad.common.Constants;
import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AdminUserDto createAdminUser(AdminUserDto adminUserDto) {
        AdminUser adminUser = new AdminUser();
        if (adminUserRepository
                .existsByUserEmailAndDelFlgFalse(
                        adminUserDto.getUserEmail())) {

            throw new DuplicateResourceException(
                    Constants.EMAIL_ID_ALREADY_EXIST
                            + adminUserDto.getUserEmail());
        }

        if (adminUserRepository
                .existsByUserIdAndDelFlgFalse(
                        adminUserDto.getUserId())) {

            throw new DuplicateResourceException(
                    Constants.USER_ID_ALREADY_EXIST
                            + adminUserDto.getUserId());
        }

        // Encrypt Password
        adminUserDto.setUserRole(AdminRole.normalize(adminUserDto.getUserRole()));
        adminUser.setUserPassword(
                passwordEncoder.encode(
                        adminUserDto.getUserPassword()));

        adminUserDto.setUserPassword(
                passwordEncoder.encode(
                        adminUserDto.getUserPassword()));

        AdminUser adminUser1 = AdminUserMapper.mapToAdminUser(adminUserDto);
        AdminUser savedAdminUser = adminUserRepository.save(adminUser1);
        return AdminUserMapper.mapToAdminUserDto(savedAdminUser);
    }

    @Override
    public AdminUserDto getAdminUserByUserId(String userId) {
    /*  AdminUser adminUser =  adminUserRepository.findByUserId(userId)
                .orElseThrow( () -> new AdminUserNotFoundException("Admin User is not Exist with Given USERID: "+userId));
        return AdminUserMapper.mapToAdminUserDto(adminUser);*/
        AdminUser adminUser = adminUserRepository
                .findByUserIdAndDelFlgFalse(userId)
                .orElseThrow(() -> new AdminUserNotFoundException("Admin User is not Exist with Given USERID: "+userId));

        return AdminUserMapper.mapToAdminUserDto(adminUser);

    }

    @Override
    public PagedResponseDto<AdminUserDto> getAllAdminUser(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable =
                PageRequest.of(pageNo - 1, pageSize);

        Page<AdminUser> userPage =
                adminUserRepository.findByDelFlgFalse(
                        pageable);

        return PaginationUtil.build(
                userPage,
                AdminUserMapper::mapToAdminUserDto);
    }

    @Override
    public AdminUserDto updateAdminUser(String userId, AdminUserDto updatedAdminUser) {
        AdminUser adminUser = adminUserRepository.findByUserId(userId).orElseThrow(() -> new AdminUserNotFoundException("Admin User is not Exist with Given USERID: "+userId));
        adminUser.setUserFName(updatedAdminUser.getUserFName());
        adminUser.setUserLName(updatedAdminUser.getUserLName());
        adminUser.setUserMName(updatedAdminUser.getUserMName());
        adminUser.setUserEmail(updatedAdminUser.getUserEmail());
        adminUser.setUserMobile(updatedAdminUser.getUserMobile());
        adminUser.setUserPostalCode(updatedAdminUser.getUserPostalCode());
        adminUser.setUserCity(updatedAdminUser.getUserCity());
        adminUser.setUserCountry(updatedAdminUser.getUserCountry());
        adminUser.setUserState(updatedAdminUser.getUserState());
        adminUser.setUserRole(AdminRole.normalize(updatedAdminUser.getUserRole()));
        //adminUser.setUserStatus(updatedAdminUser.getUserStatus());
        //adminUser.setUserId(updatedAdminUser.getUserId());

        AdminUser existingEmailUser =
                adminUserRepository
                        .findByUserEmailAndDelFlgFalse(
                                updatedAdminUser.getUserEmail())
                        .orElse(null);

        if (existingEmailUser != null
                && existingEmailUser.getUserRoleId()
                != adminUser.getUserRoleId()) {

            throw new DuplicateResourceException(
                   Constants.EMAIL_ID_ALREADY_EXIST
                            + updatedAdminUser.getUserEmail());
        }

        AdminUser existingUserId =
                adminUserRepository
                        .findByUserIdAndDelFlgFalse(
                                updatedAdminUser.getUserId())
                        .orElse(null);

        if (existingUserId != null
                && existingUserId.getUserRoleId()
                != adminUser.getUserRoleId()) {

            throw new DuplicateResourceException(
                    Constants.USER_ID_ALREADY_EXIST
                            + updatedAdminUser.getUserId());
        }

        // Update password only if supplied
        if (updatedAdminUser.getUserPassword() != null
                && !updatedAdminUser.getUserPassword().isBlank()) {

            adminUser.setUserPassword(
                    passwordEncoder.encode(
                            updatedAdminUser.getUserPassword()));
        }
        AdminUser updatedUserObj= adminUserRepository.save(adminUser);

        return AdminUserMapper.mapToAdminUserDto(updatedUserObj);

    }

    @Override
    public void deleteAdminUser(String userId) {

        AdminUser adminUser = adminUserRepository
                .findByUserIdAndDelFlgFalse(userId)
                .orElseThrow(() -> new AdminUserNotFoundException("Admin User is not Exist with Given USERID: "+userId));

        adminUser.setDelFlg(true);

        adminUserRepository.save(adminUser);
    }


}
