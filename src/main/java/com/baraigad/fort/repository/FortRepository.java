package com.baraigad.fort.repository;

import com.baraigad.fort.entity.Fort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FortRepository extends JpaRepository<Fort, Long> {

    //List<Fort> findByDelFlgFalse();
    Page<Fort> findByDelFlgFalse(Pageable pageable);
    Long countByDelFlgFalse();
    List<Fort>  findTop4ByDelFlgFalseAndStatusTrueOrderByCreatedDateDesc();

}