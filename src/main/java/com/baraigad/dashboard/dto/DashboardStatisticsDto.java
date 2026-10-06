package com.baraigad.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatisticsDto {

    private Long volunteerCount;

    private Long fortCount;

    private Long mohimCount;

    private Long galleryCount;

    private Long blogCount;

    private Long documentCount;
}