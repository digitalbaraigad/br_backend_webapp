package com.baraigad.mohim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class MohimDto {

    private Long mohimId;

    @NotBlank(message = "Mohim Name is required")
    private String mohimName;

    private String mohimNameMr;

    private String mohimType;

    private String mohimTypeMr;

    private Long fortId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    private String locationMr;

    private String description;

    private String descriptionMr;

    private String organizerName;

    private String organizerNameMr;

    private String startPoint;
    private String startPointMr;
    private String endPoint;
    private String endPointMr;
    private String distance;
    private String duration;
    private String durationMr;
    private String difficulty;
    private String difficultyMr;

    @Pattern(regexp = "^$|https?://.+", message = "WhatsApp Group Link must be a valid http or https URL")
    private String whatsappGroupLink;

    /** Ordered public image URLs for the Mohim / Event photo slider. */
    private List<String> photoUrls = new ArrayList<>();

    private String status;
}
