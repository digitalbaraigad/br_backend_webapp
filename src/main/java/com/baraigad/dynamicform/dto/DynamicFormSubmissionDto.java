package com.baraigad.dynamicform.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class DynamicFormSubmissionDto {
    private Long submissionId;
    private Long formId;
    private Map<String, String> responses;
    private boolean consentAccepted;
    private LocalDateTime submittedAt;
}
