package com.baraigad.dynamicform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class DynamicFormFieldDto {
    @NotBlank private String id;
    @NotBlank private String label;
    @NotBlank private String type;
    private boolean required;
    /** Controls whether this answer is included in the printable participant PDF. */
    private boolean includeInPdf = true;
    private String placeholder;
    private List<String> options;
}
