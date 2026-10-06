package com.baraigad.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDto {

    private Long volunteerCount;

    private Long fortCount;

    private Long mohimCount;

    private Long galleryCount;

    private Long blogCount;

    private Long registrationCount;

    private Long documentCount;
}