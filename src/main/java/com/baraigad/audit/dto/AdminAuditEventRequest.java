package com.baraigad.audit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminAuditEventRequest {
    @NotBlank(message = "Action is required") private String action;
    private String moduleName;
    private String details;
}
