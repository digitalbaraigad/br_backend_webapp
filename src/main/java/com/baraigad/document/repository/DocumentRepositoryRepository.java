package com.baraigad.document.repository;

import com.baraigad.document.entity.DocumentRepository;
import com.baraigad.gallery.entity.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepositoryRepository
        extends JpaRepository<DocumentRepository, Long> {

    Optional<DocumentRepository>
    findByDocumentIdAndDelFlgFalse(
            Long documentId);

    Page<DocumentRepository>
    findByDelFlgFalse(
            Pageable pageable);

    boolean existsByDocumentNumberAndDelFlgFalse(
            String documentNumber);

    Optional<DocumentRepository>
    findByDocumentNumberAndDelFlgFalse(
            String documentNumber);
    Long countByDelFlgFalse();

    List<DocumentRepository>
    findTop5ByDelFlgFalseOrderByCreatedDateDesc();
}