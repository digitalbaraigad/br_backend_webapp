package com.baraigad.document.mapper;

import com.baraigad.document.dto.DocumentRepositoryDto;
import com.baraigad.document.entity.DocumentRepository;

public class DocumentRepositoryMapper {

    private DocumentRepositoryMapper() {
    }

    public static DocumentRepositoryDto mapToDto(
            DocumentRepository document) {

        DocumentRepositoryDto dto =
                new DocumentRepositoryDto();

        dto.setDocumentId(
                document.getDocumentId());

        dto.setDocumentTitle(
                document.getDocumentTitle());

        dto.setDocumentType(
                document.getDocumentType());

        dto.setFortId(
                document.getFortId());

        dto.setDocumentNumber(
                document.getDocumentNumber());

        dto.setDocumentDescription(
                document.getDocumentDescription());

        dto.setDocumentUrl(
                document.getDocumentUrl());

        dto.setDocumentVersion(
                document.getDocumentVersion());

        dto.setDocumentStatus(
                document.getDocumentStatus());

        dto.setUploadedBy(
                document.getUploadedBy());

        dto.setUploadDate(
                document.getUploadDate());

        return dto;
    }

    public static DocumentRepository mapToEntity(
            DocumentRepositoryDto dto) {

        DocumentRepository document =
                new DocumentRepository();

        document.setDocumentId(
                dto.getDocumentId());

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

        return document;
    }
}