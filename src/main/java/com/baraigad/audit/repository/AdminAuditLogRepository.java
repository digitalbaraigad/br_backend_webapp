package com.baraigad.audit.repository;

import com.baraigad.audit.entity.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {
    List<AdminAuditLog> findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(LocalDateTime from);

    @Query("""
            select a from AdminAuditLog a where a.createdAt >= :from and a.createdAt < :until
            and (:email = '' or lower(coalesce(a.adminUsername, '')) = lower(:email))
            and (:search = '' or lower(coalesce(a.adminUsername, '')) like lower(concat('%', :search, '%'))
              or lower(coalesce(a.action, '')) like lower(concat('%', :search, '%'))
              or lower(coalesce(a.moduleName, '')) like lower(concat('%', :search, '%'))
              or lower(coalesce(a.resourcePath, '')) like lower(concat('%', :search, '%'))
              or lower(coalesce(a.details, '')) like lower(concat('%', :search, '%')))
            order by a.createdAt desc
            """)
    Page<AdminAuditLog> searchBetween(@Param("from") LocalDateTime from, @Param("until") LocalDateTime until, @Param("email") String email, @Param("search") String search, Pageable pageable);
}
