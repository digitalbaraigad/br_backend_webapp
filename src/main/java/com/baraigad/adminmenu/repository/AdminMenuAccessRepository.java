package com.baraigad.adminmenu.repository;

import com.baraigad.adminmenu.entity.AdminMenuAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminMenuAccessRepository extends JpaRepository<AdminMenuAccess, Long> {
    List<AdminMenuAccess> findByUserIdOrderByMenuKeyAsc(String userId);
    boolean existsByUserId(String userId);
    boolean existsByUserIdAndMenuKey(String userId, String menuKey);
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from AdminMenuAccess access where access.userId = :userId")
    int deleteByUserId(@Param("userId") String userId);
}
