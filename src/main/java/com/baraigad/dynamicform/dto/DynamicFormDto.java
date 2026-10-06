package com.baraigad.dynamicform.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DynamicFormDto {
    private Long formId;
    private String publicId;
    @NotBlank private String title;
    /** Optional image URL displayed at the top of the public form. */
    private String headerImageUrl;
    private String description;
    @NotBlank private String consentText;
    private String status = "PUBLISHED";
    /** Optional last date on which the shared public form accepts responses. */
    private LocalDate expiryDate;
    @NotEmpty @Valid private List<DynamicFormFieldDto> fields;
    private LocalDateTime createdAt;
    /** Number of completed participant submissions, shown only in the admin form list. */
    private long totalResponses;
}
