package com.baraigad.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "gallery_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "gallery_title",
                                "media_url"
                        })
        })
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_id")
    private Long galleryId;

    @Column(name = "gallery_title")
    private String galleryTitle;

    @Column(name = "gallery_title_mr")
    private String galleryTitleMr;

    @Column(name = "gallery_description", columnDefinition = "TEXT")
    private String galleryDescription;

    @Column(name = "gallery_description_mr", columnDefinition = "TEXT")
    private String galleryDescriptionMr;

    @Column(name = "fort_id")
    private Long fortId;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "media_type")
    private String mediaType;

    /** PHOTO, VIDEO, or EVENT. This controls the public Gallery submenu. */
    @Column(name = "gallery_category")
    private String galleryCategory;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "capture_date")
    private LocalDate captureDate;

    @Column(name = "photographer_name")
    private String photographerName;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "del_flg")
    private Boolean delFlg = false;
}
