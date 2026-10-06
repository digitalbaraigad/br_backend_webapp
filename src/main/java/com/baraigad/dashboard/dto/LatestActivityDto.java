package com.baraigad.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LatestActivityDto {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private String activityDate;
}