package com.baraigad.enquiry.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactEnquiryDto {
    private Long enquiryId;

    @NotBlank(message = "Full name is required")
    @Size(max = 150)
    private String fullName;

    @NotBlank(message = "Email ID is required")
    @Email(message = "Enter a valid email ID")
    private String emailId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must contain 10 to 15 digits")
    private String phoneNumber;

    @NotBlank(message = "WhatsApp phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "WhatsApp phone number must contain 10 to 15 digits")
    private String whatsappPhoneNumber;

    @NotBlank(message = "Message is required")
    @Size(max = 700)
    private String message;

    private LocalDateTime createdAt;
}
