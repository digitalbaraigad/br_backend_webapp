package com.baraigad.analytics.repository;

import com.baraigad.analytics.entity.WebsiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface WebsiteVisitRepository extends JpaRepository<WebsiteVisit, Long> {
    long countByVisitedAtGreaterThanEqualAndVisitedAtLessThan(LocalDateTime start, LocalDateTime end);
}
