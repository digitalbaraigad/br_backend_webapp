package com.baraigad.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "admin_audit_log", indexes = {
        @Index(name = "idx_admin_audit_created", columnList = "created_at"),
        @Index(name = "idx_admin_audit_user", columnList = "admin_username")
})
public class AdminAuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id") private Long auditId;
    @Column(name = "admin_username", length = 150) private String adminUsername;
    @Column(name = "action", nullable = false, length = 40) private String action;
    @Column(name = "module_name", length = 100) private String moduleName;
    @Column(name = "request_method", length = 12) private String requestMethod;
    @Column(name = "resource_path", length = 500) private String resourcePath;
    @Column(name = "details", length = 2000) private String details;
    @Column(name = "ip_address", length = 80) private String ipAddress;
    @Column(name = "response_status") private Integer responseStatus;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
}
