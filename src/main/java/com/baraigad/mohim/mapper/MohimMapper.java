package com.baraigad.mohim.mapper;

import com.baraigad.mohim.dto.MohimDto;
import com.baraigad.mohim.entity.Mohim;
import java.util.ArrayList;

public class MohimMapper {

    private MohimMapper() {
    }

    public static MohimDto mapToDto(
            Mohim mohim) {

        MohimDto dto = new MohimDto();

        dto.setMohimId(mohim.getMohimId());
        dto.setMohimName(mohim.getMohimName());
        dto.setMohimNameMr(mohim.getMohimNameMr());
        dto.setMohimType(mohim.getMohimType());
        dto.setMohimTypeMr(mohim.getMohimTypeMr());
        dto.setFortId(mohim.getFortId());
        dto.setStartDate(mohim.getStartDate());
        dto.setEndDate(mohim.getEndDate());
        dto.setLocation(mohim.getLocation());
        dto.setLocationMr(mohim.getLocationMr());
        dto.setDescription(mohim.getDescription());
        dto.setDescriptionMr(mohim.getDescriptionMr());
        dto.setOrganizerName(mohim.getOrganizerName());
        dto.setOrganizerNameMr(mohim.getOrganizerNameMr());
        dto.setStartPoint(mohim.getStartPoint());
        dto.setStartPointMr(mohim.getStartPointMr());
        dto.setEndPoint(mohim.getEndPoint());
        dto.setEndPointMr(mohim.getEndPointMr());
        dto.setDistance(mohim.getDistance());
        dto.setDuration(mohim.getDuration());
        dto.setDurationMr(mohim.getDurationMr());
        dto.setDifficulty(mohim.getDifficulty());
        dto.setDifficultyMr(mohim.getDifficultyMr());
        dto.setWhatsappGroupLink(mohim.getWhatsappGroupLink());
        dto.setPhotoUrls(new ArrayList<>(mohim.getPhotoUrls()));
        dto.setStatus(mohim.getStatus());

        return dto;
    }

    public static Mohim mapToEntity(
            MohimDto dto) {

        Mohim mohim = new Mohim();

        mohim.setMohimId(dto.getMohimId());
        mohim.setMohimName(dto.getMohimName());
        mohim.setMohimNameMr(dto.getMohimNameMr());
        mohim.setMohimType(dto.getMohimType());
        mohim.setMohimTypeMr(dto.getMohimTypeMr());
        mohim.setFortId(dto.getFortId());
        mohim.setStartDate(dto.getStartDate());
        mohim.setEndDate(dto.getEndDate());
        mohim.setLocation(dto.getLocation());
        mohim.setLocationMr(dto.getLocationMr());
        mohim.setDescription(dto.getDescription());
        mohim.setDescriptionMr(dto.getDescriptionMr());
        mohim.setOrganizerName(dto.getOrganizerName());
        mohim.setOrganizerNameMr(dto.getOrganizerNameMr());
        mohim.setStartPoint(dto.getStartPoint());
        mohim.setStartPointMr(dto.getStartPointMr());
        mohim.setEndPoint(dto.getEndPoint());
        mohim.setEndPointMr(dto.getEndPointMr());
        mohim.setDistance(dto.getDistance());
        mohim.setDuration(dto.getDuration());
        mohim.setDurationMr(dto.getDurationMr());
        mohim.setDifficulty(dto.getDifficulty());
        mohim.setDifficultyMr(dto.getDifficultyMr());
        mohim.setWhatsappGroupLink(dto.getWhatsappGroupLink());
        mohim.setPhotoUrls(dto.getPhotoUrls() == null ? new ArrayList<>() : new ArrayList<>(dto.getPhotoUrls()));
        mohim.setStatus(dto.getStatus());

        return mohim;
    }
}
