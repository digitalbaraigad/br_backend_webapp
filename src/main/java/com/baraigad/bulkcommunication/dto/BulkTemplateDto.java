package com.baraigad.bulkcommunication.dto;

import java.util.List;

public record BulkTemplateDto(
        String code,
        String label,
        String templateName,
        String languageCode,
        List<String> variables
) {}
