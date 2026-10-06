package com.baraigad.gallery.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.gallery.dto.GalleryDto;

public interface GalleryService {

    GalleryDto createGallery(
            GalleryDto dto);

    GalleryDto getGalleryById(
            Long galleryId);

    PagedResponseDto<GalleryDto>
    getAllGalleries(
            Integer pageNo,
            Integer pageSize);

    PagedResponseDto<GalleryDto>
    getGalleriesByCategory(
            String galleryCategory,
            Integer pageNo,
            Integer pageSize);

    GalleryDto updateGallery(
            Long galleryId,
            GalleryDto dto);

    GalleryDto addMedia(
            Long galleryId,
            String mediaUrl,
            String mediaType);

    GalleryDto removeMedia(
            Long galleryId);

    GalleryDto removeMedia(Long galleryId, Long galleryMediaId);

    void deleteGallery(
            Long galleryId);
}
