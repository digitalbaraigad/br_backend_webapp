package com.baraigad.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WebsiteVisitRequest {
    @NotBlank(message = "Visitor identifier is required")
    @Size(max = 64, message = "Visitor identifier is too long")
    private String visitorId;
}
