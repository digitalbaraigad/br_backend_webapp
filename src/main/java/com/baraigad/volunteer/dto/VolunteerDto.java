package com.baraigad.volunteer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VolunteerDto {

    private Integer volunteerId;
    private String volunteerFName;
    private String volunteerMName;
    private String volunteerLName;
    private String volunteerEmail;
    @NotBlank(message = "Mobile number is required for volunteer registration")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Mobile number must be a valid 10-digit Indian mobile number"
    )
    private String volunteerMobile;
    private String volunteerCity;
    private String volunteerDistrict;
    private String volunteerTaluka;
    private String volunteerState;
    private String volunteerCountry;
    private String volunteerPostalCode;
    private LocalDate volunteerDOB;
    private String volunteerOccupation;
    private String volunteerAvailability;
    private String volunteerPassportPhotoUrl;
    private Boolean delFlag;
}
