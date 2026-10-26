package com.baraigad.trail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TrekRepository extends JpaRepository<TrekHistory, Long> {
    Optional<TrekHistory> findByOwnerEmailAndClientId(String ownerEmail, String clientId);
    Page<TrekHistory> findByOwnerEmailOrderByIdDesc(String ownerEmail, Pageable page);
}
