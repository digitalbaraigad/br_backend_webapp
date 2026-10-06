package com.baraigad.document.controller;

import com.baraigad.common.Constants;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.document.dto.DocumentRepositoryDto;
import com.baraigad.document.service.DocumentRepositoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.DOCUMENT_API)
@RequiredArgsConstructor
public class DocumentRepositoryController {

    private final DocumentRepositoryService
            documentRepositoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentRepositoryDto>>
    createDocument(

            @Valid
            @RequestBody
            DocumentRepositoryDto dto) {

        return ResponseUtil.created(
                "Document created successfully",
                documentRepositoryService
                        .createDocument(dto));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<ApiResponse<DocumentRepositoryDto>>
    getDocumentById(

            @PathVariable
            Long documentId) {

        return ResponseUtil.success(
                "Document fetched successfully",
                documentRepositoryService
                        .getDocumentById(
                                documentId));
    }

    @GetMapping
    public ResponseEntity<
            ApiResponse<
                    PagedResponseDto<
                            DocumentRepositoryDto>>>
    getAllDocuments(

            @RequestParam(
                    defaultValue = "1")
            Integer pageNo,

            @RequestParam(
                    defaultValue = "20")
            Integer pageSize) {

        return ResponseUtil.success(
                "Documents fetched successfully",
                documentRepositoryService
                        .getAllDocuments(
                                pageNo,
                                pageSize));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<ApiResponse<DocumentRepositoryDto>>
    updateDocument(

            @PathVariable
            Long documentId,

            @Valid
            @RequestBody
            DocumentRepositoryDto dto) {

        return ResponseUtil.success(
                "Document updated successfully",
                documentRepositoryService
                        .updateDocument(
                                documentId,
                                dto));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse<Object>>
    deleteDocument(

            @PathVariable
            Long documentId) {

        documentRepositoryService
                .deleteDocument(
                        documentId);

        return ResponseUtil.success(
                "Document deleted successfully",
                null);
    }
}