package com.baraigad.workbanner.repository;

import com.baraigad.workbanner.entity.WorkBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkBannerRepository extends JpaRepository<WorkBanner, Long> {
    List<WorkBanner> findByPageKeyOrderByDisplayOrderAscBannerIdAsc(String pageKey);
    long countByPageKey(String pageKey);
}
