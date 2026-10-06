package com.baraigad.dashboard.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDto {

    private DashboardStatisticsDto statistics;

    private BannerDto banner;

    private List<UpcomingMohimDto> upcomingMohims;

    private List<RecentBlogDto> recentBlogs;

    private List<RecentGalleryDto> recentGallery;

    private List<RecentDocumentDto> recentDocuments;

    private List<FeaturedFortDto> featuredForts;
}