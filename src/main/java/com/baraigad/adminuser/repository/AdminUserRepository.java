package com.baraigad.adminuser.repository;

import com.baraigad.adminuser.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
    Optional<AdminUser> findByUserId(String userId);
    Optional<AdminUser> findByUserIdAndDelFlgFalse(String userId);
    Optional<AdminUser> findByUserEmailAndDelFlgFalse(
            String userEmail);
    Page<AdminUser> findByDelFlgFalse(
            Pageable pageable);
    Optional<AdminUser> deleteByUserIdAndDelFlgFalse(String userId);
    Optional<AdminUser> findByUserEmail(
            String userEmail);
    boolean existsByUserEmailAndDelFlgFalse(
            String userEmail);

    boolean existsByUserIdAndDelFlgFalse(
            String userId);

    boolean existsByUserRoleAndDelFlgFalse(String userRole);

}
