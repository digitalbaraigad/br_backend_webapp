package com.baraigad.fortguide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface FortGuideRepository extends JpaRepository<FortGuide, Long> {
    Page<FortGuide> findByPublishedTrue(Pageable page);
}
