package com.baraigad.gallery.repository;

import com.baraigad.gallery.entity.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository
        extends JpaRepository<Gallery, Long> {

    Optional<Gallery>
    findByGalleryIdAndDelFlgFalse(
            Long galleryId);

    Page<Gallery>
    findByDelFlgFalse(
            Pageable pageable);

    @Query("""
            select g from Gallery g
            where g.delFlg = false
              and (upper(g.galleryCategory) = upper(:galleryCategory)
                   or (:galleryCategory = 'PHOTO' and g.galleryCategory is null))
            """)
    Page<Gallery> findPublicByCategory(
            @Param("galleryCategory") String galleryCategory,
            Pageable pageable);

    boolean existsByGalleryTitleAndMediaUrlAndDelFlgFalse(
            String galleryTitle,
            String mediaUrl);

    List<Gallery>
    findTop6ByDelFlgFalseAndStatusOrderByCreatedDateDesc(
            String status);

    Long countByDelFlgFalse();
}
