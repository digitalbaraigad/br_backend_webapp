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
public class RecentGalleryDto {

    private Long galleryId;

    private String galleryTitle;

    private String mediaUrl;

    private String thumbnailUrl;

    private String mediaType;
}