package com.baraigad.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActivityRegistrationRequestDto {

    @NotBlank(message = "Mobile number is required for activity registration")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Mobile number must be a valid 10-digit Indian mobile number"
    )
    private String mobileNumber;

    @NotNull(message = "Activity id is required")
    private Long activityId;

    private String remarks;
}
