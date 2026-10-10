package com.baraigad.fort.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FortDto {

    private Long fortId;
    private String fortName;
    private String district;
    private String districtMr;
    private String state;
    private Double elevation;
    private String latitude;
    private String longitude;
    private String descriptionEn;
    private String descriptionMr;
    private String fortTitleEn;
    private String fortTitleMr;
    private String headingEn;
    private String headingMr;
    private String subHeadingEn;
    private String subHeadingMr;
    private String aboutHeadingEn;
    private String aboutHeadingMr;
    private String fortTypeEn;
    private String fortTypeMr;
    private String conservationStatusEn;
    private String conservationStatusMr;
    private Long coverMediaId;
    private List<String> mediaUrls = new ArrayList<>();
    private Boolean status;
}
