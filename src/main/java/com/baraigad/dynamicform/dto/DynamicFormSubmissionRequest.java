package com.baraigad.dynamicform.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class DynamicFormSubmissionRequest {
    @NotNull private Map<String, String> responses;
    @AssertTrue(message = "Participant consent is required") private boolean consentAccepted;
}
