package com.baraigad.mohim.repository;

import com.baraigad.mohim.entity.Mohim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MohimRepository
        extends JpaRepository<Mohim, Long> {

    Optional<Mohim> findByMohimIdAndDelFlgFalse(
            Long mohimId);

    Page<Mohim> findByDelFlgFalse(
            Pageable pageable);

    boolean existsByMohimNameAndDelFlgFalse(
            String mohimName);

    Optional<Mohim> findByMohimNameAndDelFlgFalse(
            String mohimName);
    Long countByDelFlgFalse();
    List<Mohim>
    findTop3ByDelFlgFalseAndStartDateGreaterThanEqualOrderByStartDateAsc(
            LocalDate currentDate);
}