package com.baraigad.fort.mapper;

import com.baraigad.fort.dto.FortDto;
import com.baraigad.fort.entity.Fort;
import java.util.ArrayList;

public class FortMapper {

    public static FortDto mapToDto(Fort fort) {

        return FortDto.builder()
                .fortId(fort.getFortId())
                .fortName(fort.getFortName())
                .district(fort.getDistrict())
                .state(fort.getState())
                .elevation(fort.getElevation())
                .latitude(fort.getLatitude())
                .longitude(fort.getLongitude())
                .descriptionEn(fort.getDescriptionEn())
                .descriptionMr(fort.getDescriptionMr())
                .coverMediaId(fort.getCoverMediaId())
                .mediaUrls(fort.getMediaUrls() == null ? new ArrayList<>() : new ArrayList<>(fort.getMediaUrls()))
                .status(fort.getStatus())
                .fortTitleMr(fort.getFortTitleMr())
                .fortTitleEn(fort.getFortTitleEn())
                .headingEn(fort.getHeadingEn())
                .headingMr(fort.getHeadingMr())
                .subHeadingEn(fort.getSubHeadingEn())
                .subHeadingMr(fort.getSubHeadingMr())
                .aboutHeadingEn(fort.getAboutHeadingEn())
                .aboutHeadingMr(fort.getAboutHeadingMr())
                .fortTypeEn(fort.getFortTypeEn())
                .fortTypeMr(fort.getFortTypeMr())
                .conservationStatusEn(fort.getConservationStatusEn())
                .conservationStatusMr(fort.getConservationStatusMr())
                .build();
    }

    public static Fort mapToEntity(FortDto dto) {

        return Fort.builder()
                .fortId(dto.getFortId())
                .fortName(dto.getFortName())
                .district(dto.getDistrict())
                .state(dto.getState())
                .elevation(dto.getElevation())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .descriptionEn(dto.getDescriptionEn())
                .descriptionMr(dto.getDescriptionMr())
                .coverMediaId(dto.getCoverMediaId())
                .mediaUrls(dto.getMediaUrls() == null ? new ArrayList<>() : new ArrayList<>(dto.getMediaUrls()))
                .fortTitleMr(dto.getFortTitleMr())
                .fortTitleEn(dto.getFortTitleEn())
                .headingEn(dto.getHeadingEn())
                .headingMr(dto.getHeadingMr())
                .subHeadingEn(dto.getSubHeadingEn())
                .subHeadingMr(dto.getSubHeadingMr())
                .aboutHeadingEn(dto.getAboutHeadingEn())
                .aboutHeadingMr(dto.getAboutHeadingMr())
                .fortTypeEn(dto.getFortTypeEn())
                .fortTypeMr(dto.getFortTypeMr())
                .conservationStatusEn(dto.getConservationStatusEn())
                .conservationStatusMr(dto.getConservationStatusMr())
                .status(dto.getStatus())
                .build();
    }
}
