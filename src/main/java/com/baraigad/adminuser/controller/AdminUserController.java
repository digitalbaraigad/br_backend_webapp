package com.baraigad.adminuser.controller;

import com.baraigad.adminuser.dto.AdminUserDto;
import com.baraigad.adminuser.service.AdminUserService;
import com.baraigad.common.Constants;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ListResponseDto;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(Constants.ADMIN_USER_API)
public class AdminUserController {

    private final AdminUserService adminUserService;

    //Build REST API for Create User
    @PostMapping
    public ResponseEntity<ApiResponse<AdminUserDto>> createAdminUser(
            @RequestBody AdminUserDto request) {
        AdminUserDto savedAdminUser =
                adminUserService.createAdminUser(request);
        return ResponseUtil.created(
                Constants.ADMINUSER_CREATED_SUCCESSFULLY,
                savedAdminUser
        );
    }

    //Build REST API for UserGet User By userId

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserDto>> getAdminUserByUserId(
            @PathVariable String userId) {

        AdminUserDto adminUserDto =
                adminUserService.getAdminUserByUserId(userId);

        return ResponseUtil.success(
                Constants.ADMINUSERS_RETRIEVED_SUCCESSFULLY,
                adminUserDto
        );
    }


    @GetMapping
    public ResponseEntity<
            ApiResponse<PagedResponseDto<AdminUserDto>>>
    getAllAdminUser(

            @RequestParam(defaultValue = Constants.PAGE_NO)
            Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE)
            Integer pageSize) {

        return ResponseUtil.success(
                Constants.ADMINUSERS_RETRIEVED_SUCCESSFULLY,
                adminUserService.getAllAdminUser(
                        pageNo,
                        pageSize));
    }

    //Build REST API for Update User

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserDto>> updateAdminUser(
            @PathVariable String userId,
            @RequestBody AdminUserDto adminUserDto) {

        AdminUserDto updatedAdminUserDto =
                adminUserService.updateAdminUser(userId, adminUserDto);

        return ResponseUtil.success(
                Constants.ADMINUSER_UPDATED_SUCCESSFULLY,
                updatedAdminUserDto
        );
    }

    //Build REST API for delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteAdminUser(
            @PathVariable String userId) {

        adminUserService.deleteAdminUser(userId);

        return ResponseUtil.success(
                Constants.ADMINUSER_DELETED_SUCCESSFULLY,
                null
        );
    }
}