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
public class UpcomingMohimDto {

    private Long mohimId;

    private String mohimName;

    private String mohimNameMr;

    private String location;

    private String locationMr;

    private String startDate;

    private String endDate;
}
