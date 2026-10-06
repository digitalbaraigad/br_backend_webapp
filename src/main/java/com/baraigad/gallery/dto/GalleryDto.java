package com.baraigad.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GalleryDto {

    private Long galleryId;

    @NotBlank
    private String galleryTitle;

    @NotBlank
    private String galleryTitleMr;

    @Size(max = 250, message = "English description must not exceed 250 characters")
    private String galleryDescription;

    @Size(max = 250, message = "Marathi description must not exceed 250 characters")
    private String galleryDescriptionMr;

    private Long fortId;

    private String mediaUrl;

    /** All images/videos belonging to this gallery item. mediaUrl remains the cover for compatibility. */
    private List<String> mediaUrls;

    private String mediaType;

    private String galleryCategory;

    private String thumbnailUrl;

    private LocalDate captureDate;

    private String photographerName;

    private String status;
}
