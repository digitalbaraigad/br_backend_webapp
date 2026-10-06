package com.baraigad.gallery.mapper;

import com.baraigad.gallery.dto.GalleryDto;
import com.baraigad.gallery.entity.Gallery;

public class GalleryMapper {

    private GalleryMapper() {}

    public static GalleryDto mapToDto(
            Gallery gallery) {

        GalleryDto dto =
                new GalleryDto();

        dto.setGalleryId(
                gallery.getGalleryId());

        dto.setGalleryTitle(
                gallery.getGalleryTitle());

        dto.setGalleryTitleMr(
                gallery.getGalleryTitleMr());

        dto.setGalleryDescription(
                gallery.getGalleryDescription());

        dto.setGalleryDescriptionMr(
                gallery.getGalleryDescriptionMr());

        dto.setFortId(
                gallery.getFortId());

        dto.setMediaUrl(
                gallery.getMediaUrl());

        dto.setMediaType(
                gallery.getMediaType());

        dto.setGalleryCategory(
                gallery.getGalleryCategory());

        dto.setThumbnailUrl(
                gallery.getThumbnailUrl());

        dto.setCaptureDate(
                gallery.getCaptureDate());

        dto.setPhotographerName(
                gallery.getPhotographerName());

        dto.setStatus(
                gallery.getStatus());

        return dto;
    }

    public static Gallery mapToEntity(
            GalleryDto dto) {

        Gallery gallery =
                new Gallery();

        gallery.setGalleryId(
                dto.getGalleryId());

        gallery.setGalleryTitle(
                dto.getGalleryTitle());

        gallery.setGalleryTitleMr(
                dto.getGalleryTitleMr());

        gallery.setGalleryDescription(
                dto.getGalleryDescription());

        gallery.setGalleryDescriptionMr(
                dto.getGalleryDescriptionMr());

        gallery.setFortId(
                dto.getFortId());

        gallery.setMediaUrl(
                dto.getMediaUrl());

        gallery.setMediaType(
                dto.getMediaType());

        gallery.setGalleryCategory(
                dto.getGalleryCategory());

        gallery.setThumbnailUrl(
                dto.getThumbnailUrl());

        gallery.setCaptureDate(
                dto.getCaptureDate());

        gallery.setPhotographerName(
                dto.getPhotographerName());

        gallery.setStatus(
                dto.getStatus());

        return gallery;
    }
}
