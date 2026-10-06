package com.baraigad.document.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.document.dto.DocumentRepositoryDto;

public interface DocumentRepositoryService {

    DocumentRepositoryDto createDocument(
            DocumentRepositoryDto dto);

    DocumentRepositoryDto getDocumentById(
            Long documentId);

    PagedResponseDto<DocumentRepositoryDto>
    getAllDocuments(
            Integer pageNo,
            Integer pageSize);

    DocumentRepositoryDto updateDocument(
            Long documentId,
            DocumentRepositoryDto dto);

    void deleteDocument(
            Long documentId);
}