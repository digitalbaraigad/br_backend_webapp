package com.baraigad.workbanner.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkBannerDto {
    private Long bannerId;
    private String pageKey;
    private String imageUrl;
    private Integer displayOrder;
}
