package com.baraigad.participant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipantDto {
    private Long participantId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be a future date")
    private LocalDate dob;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10-digit Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Taluka is required")
    private String taluka;

    @NotBlank(message = "Email ID is required")
    @Email(message = "Enter a valid email ID")
    private String emailId;

    @NotBlank(message = "Interested activity is required")
    private String interestedActivity;

    private String volunteerMobile;
    private Integer linkedVolunteerId;
}
