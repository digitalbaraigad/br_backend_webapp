package com.baraigad.audit.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminAuditLogDto {
    private Long auditId;
    private String adminUsername;
    private String action;
    private String moduleName;
    private String requestMethod;
    private String resourcePath;
    private String details;
    private String ipAddress;
    private Integer responseStatus;
    private LocalDateTime createdAt;
}
