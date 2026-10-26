package com.baraigad.trail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrailRepository extends JpaRepository<TrailRoute, Long> {
    Page<TrailRoute> findByPublishedTrue(Pageable pageable);
}
