package com.baraigad.document.service;

import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.document.dto.DocumentRepositoryDto;
import com.baraigad.document.entity.DocumentRepository;
import com.baraigad.document.mapper.DocumentRepositoryMapper;
import com.baraigad.document.repository.DocumentRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentRepositoryServiceImpl
        implements DocumentRepositoryService {

    private final DocumentRepositoryRepository
            documentRepository;

    @Override
    public DocumentRepositoryDto createDocument(
            DocumentRepositoryDto dto) {

        if (documentRepository
                .existsByDocumentNumberAndDelFlgFalse(
                        dto.getDocumentNumber())) {

            throw new DuplicateResourceException(
                    "Document Number already exists : "
                            + dto.getDocumentNumber());
        }

        DocumentRepository document =
                DocumentRepositoryMapper
                        .mapToEntity(dto);

        document.setCreatedDate(
                LocalDateTime.now());

        document.setDelFlg(false);

        DocumentRepository savedDocument =
                documentRepository.save(
                        document);

        return DocumentRepositoryMapper
                .mapToDto(savedDocument);
    }

    @Override
    public DocumentRepositoryDto getDocumentById(
            Long documentId) {

        DocumentRepository document =
                documentRepository
                        .findByDocumentIdAndDelFlgFalse(
                                documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"));

        return DocumentRepositoryMapper
                .mapToDto(document);
    }

    @Override
    public PagedResponseDto<DocumentRepositoryDto>
    getAllDocuments(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable =
                PageRequest.of(
                        pageNo - 1,
                        pageSize);

        Page<DocumentRepository> page =
                documentRepository
                        .findByDelFlgFalse(
                                pageable);

        return PaginationUtil.build(
                page,
                DocumentRepositoryMapper::mapToDto);
    }

    @Override
    public DocumentRepositoryDto updateDocument(
            Long documentId,
            DocumentRepositoryDto dto) {

        DocumentRepository document =
                documentRepository
                        .findByDocumentIdAndDelFlgFalse(
                                documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"));

        DocumentRepository existingDocument =
                documentRepository
                        .findByDocumentNumberAndDelFlgFalse(
                                dto.getDocumentNumber())
                        .orElse(null);

        if (existingDocument != null
                && !existingDocument
                .getDocumentId()
                .equals(documentId)) {

            throw new DuplicateResourceException(
                    "Document Number already exists : "
                            + dto.getDocumentNumber());
        }

        document.setDocumentTitle(
                dto.getDocumentTitle());

        document.setDocumentType(
                dto.getDocumentType());

        document.setFortId(
                dto.getFortId());

        document.setDocumentNumber(
                dto.getDocumentNumber());

        document.setDocumentDescription(
                dto.getDocumentDescription());

        document.setDocumentUrl(
                dto.getDocumentUrl());

        document.setDocumentVersion(
                dto.getDocumentVersion());

        document.setDocumentStatus(
                dto.getDocumentStatus());

        document.setUploadedBy(
                dto.getUploadedBy());

        document.setUploadDate(
                dto.getUploadDate());

        document.setUpdatedDate(
                LocalDateTime.now());

        DocumentRepository updatedDocument =
                documentRepository.save(
                        document);

        return DocumentRepositoryMapper
                .mapToDto(updatedDocument);
    }

    @Override
    public void deleteDocument(
            Long documentId) {

        DocumentRepository document =
                documentRepository
                        .findByDocumentIdAndDelFlgFalse(
                                documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found"));

        document.setDelFlg(true);

        document.setUpdatedDate(
                LocalDateTime.now());

        documentRepository.save(
                document);
    }
}