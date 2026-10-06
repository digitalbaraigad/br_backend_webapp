package com.baraigad.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardBannerDto {

    private String title;

    private String subTitle;

    private String bannerImage;
}