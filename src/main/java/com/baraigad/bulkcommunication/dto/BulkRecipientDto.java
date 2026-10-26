package com.baraigad.bulkcommunication.dto;

public record BulkRecipientDto(
        String recipientKey,
        String source,
        String sourceLabel,
        String name,
        String mobile,
        String email,
        String district,
        String taluka,
        String formTitle
) {}
