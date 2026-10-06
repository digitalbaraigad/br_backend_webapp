package com.baraigad.document.dto;

import com.baraigad.document.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import com.baraigad.document.enums.DocumentType;
@Data
public class DocumentRepositoryDto {

    private Long documentId;

    @NotBlank
    private String documentTitle;



    private Long fortId;

    @NotBlank
    private String documentNumber;

    private String documentDescription;

    private String documentUrl;

    private String documentVersion;

    private String documentStatus;

    private String uploadedBy;

    private LocalDate uploadDate;



    private DocumentType documentType;
}