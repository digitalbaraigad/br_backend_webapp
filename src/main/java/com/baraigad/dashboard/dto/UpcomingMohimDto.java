package com.baraigad.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private String cardImageUrl;

    private List<String> photoUrls;
}
