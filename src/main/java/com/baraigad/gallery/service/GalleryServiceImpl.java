package com.baraigad.gallery.service;

import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.gallery.dto.GalleryDto;
import com.baraigad.gallery.entity.Gallery;
import com.baraigad.gallery.mapper.GalleryMapper;
import com.baraigad.gallery.repository.GalleryRepository;
import com.baraigad.gallery.repository.GalleryMediaRepository;
import com.baraigad.gallery.entity.GalleryMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl
        implements GalleryService {

    private final GalleryRepository galleryRepository;
    private final GalleryMediaRepository galleryMediaRepository;

    @Override
    public GalleryDto createGallery(
            GalleryDto dto) {

        if (galleryRepository
                .existsByGalleryTitleAndMediaUrlAndDelFlgFalse(
                        dto.getGalleryTitle(),
                        dto.getMediaUrl())) {

            throw new DuplicateResourceException(
                    "Gallery already exists");
        }

        Gallery gallery =
                GalleryMapper.mapToEntity(dto);

        gallery.setGalleryCategory(normalizeCategory(dto.getGalleryCategory()));

        gallery.setCreatedDate(
                LocalDateTime.now());

        gallery.setDelFlg(false);

        return toDto(galleryRepository.save(gallery));
    }

    @Override
    public GalleryDto getGalleryById(
            Long galleryId) {

        Gallery gallery =
                galleryRepository
                        .findByGalleryIdAndDelFlgFalse(
                                galleryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Gallery not found"));

        return toDto(gallery);
    }

    @Override
    public PagedResponseDto<GalleryDto>
    getAllGalleries(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable =
                PageRequest.of(
                        pageNo - 1,
                        pageSize);

        Page<Gallery> page =
                galleryRepository
                        .findByDelFlgFalse(
                                pageable);

        return PaginationUtil.build(
                page,
                this::toDto);
    }

    @Override
    public PagedResponseDto<GalleryDto>
    getGalleriesByCategory(
            String galleryCategory,
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Gallery> page = galleryRepository.findPublicByCategory(
                normalizeCategory(galleryCategory), pageable);
        return PaginationUtil.build(page, this::toDto);
    }

    @Override
    public GalleryDto updateGallery(
            Long galleryId,
            GalleryDto dto) {

        Gallery gallery =
                galleryRepository
                        .findByGalleryIdAndDelFlgFalse(
                                galleryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Gallery not found"));

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
                normalizeCategory(dto.getGalleryCategory()));

        gallery.setThumbnailUrl(
                dto.getThumbnailUrl());

        gallery.setCaptureDate(
                dto.getCaptureDate());

        gallery.setPhotographerName(
                dto.getPhotographerName());

        gallery.setStatus(
                dto.getStatus());

        gallery.setUpdatedDate(
                LocalDateTime.now());

        return toDto(galleryRepository.save(gallery));
    }

    @Override
    public void deleteGallery(
            Long galleryId) {

        Gallery gallery =
                galleryRepository
                        .findByGalleryIdAndDelFlgFalse(
                                galleryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Gallery not found"));

        gallery.setDelFlg(true);

        gallery.setUpdatedDate(
                LocalDateTime.now());

        galleryRepository.save(
                gallery);
    }

    @Override
    public GalleryDto addMedia(Long galleryId, String mediaUrl, String mediaType) {
        Gallery gallery = galleryRepository.findByGalleryIdAndDelFlgFalse(galleryId)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found"));
        GalleryMedia item = new GalleryMedia();
        item.setGallery(gallery);
        item.setMediaUrl(mediaUrl);
        item.setMediaType(mediaType);
        galleryMediaRepository.save(item);
        // Preserve the first uploaded file as cover so older clients continue to work.
        if (gallery.getMediaUrl() == null || gallery.getMediaUrl().isBlank()) {
            gallery.setMediaUrl(mediaUrl);
            gallery.setMediaType(mediaType);
        }
        gallery.setUpdatedDate(LocalDateTime.now());
        return toDto(galleryRepository.save(gallery));
    }

    @Override
    public GalleryDto removeMedia(Long galleryId) {
        Gallery gallery = galleryRepository.findByGalleryIdAndDelFlgFalse(galleryId)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found"));
        galleryMediaRepository.deleteByGalleryGalleryId(galleryId);
        gallery.setMediaUrl(null);
        gallery.setThumbnailUrl(null);
        gallery.setUpdatedDate(LocalDateTime.now());
        return toDto(galleryRepository.save(gallery));
    }

    @Override
    public GalleryDto removeMedia(Long galleryId, Long galleryMediaId) {
        Gallery gallery = galleryRepository.findByGalleryIdAndDelFlgFalse(galleryId)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found"));
        GalleryMedia item = galleryMediaRepository.findById(galleryMediaId)
                .filter(media -> media.getGallery().getGalleryId().equals(galleryId))
                .orElseThrow(() -> new ResourceNotFoundException("Gallery attachment not found"));
        galleryMediaRepository.delete(item);
        var remaining = galleryMediaRepository.findByGalleryGalleryIdOrderByGalleryMediaIdAsc(galleryId);
        if (remaining.isEmpty()) {
            gallery.setMediaUrl(null);
            gallery.setThumbnailUrl(null);
        } else {
            gallery.setMediaUrl(remaining.get(0).getMediaUrl());
            gallery.setMediaType(remaining.get(0).getMediaType());
        }
        gallery.setUpdatedDate(LocalDateTime.now());
        return toDto(galleryRepository.save(gallery));
    }

    private GalleryDto toDto(Gallery gallery) {
        GalleryDto dto = GalleryMapper.mapToDto(gallery);
        var media = galleryMediaRepository.findByGalleryGalleryIdOrderByGalleryMediaIdAsc(gallery.getGalleryId());
        dto.setMediaUrls(media.stream().map(GalleryMedia::getMediaUrl).toList());
        if (dto.getMediaUrls().isEmpty() && gallery.getMediaUrl() != null && !gallery.getMediaUrl().isBlank()) {
            dto.setMediaUrls(java.util.List.of(gallery.getMediaUrl()));
        }
        return dto;
    }

    private String normalizeCategory(String galleryCategory) {
        String value = galleryCategory == null || galleryCategory.isBlank()
                ? "PHOTO"
                : galleryCategory.trim().toUpperCase(Locale.ROOT);
        if (!value.equals("PHOTO") && !value.equals("VIDEO") && !value.equals("EVENT")) {
            throw new IllegalArgumentException("Gallery category must be PHOTO, VIDEO, or EVENT.");
        }
        return value;
    }
}
