package com.baraigad.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gallery_media")
public class GalleryMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_media_id")
    private Long galleryMediaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @Column(name = "media_type")
    private String mediaType;
}
