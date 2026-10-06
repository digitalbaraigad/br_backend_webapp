package com.baraigad.gallery.repository;

import com.baraigad.gallery.entity.GalleryMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GalleryMediaRepository extends JpaRepository<GalleryMedia, Long> {
    List<GalleryMedia> findByGalleryGalleryIdOrderByGalleryMediaIdAsc(Long galleryId);
    void deleteByGalleryGalleryId(Long galleryId);
}
