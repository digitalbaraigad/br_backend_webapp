package com.baraigad.document.entity;

import com.baraigad.document.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import com.baraigad.document.enums.DocumentType;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "document_repository",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "document_number")
        })
public class DocumentRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_title")
    private String documentTitle;


    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "fort_id")
    private Long fortId;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_description")
    private String documentDescription;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "document_version")
    private String documentVersion;

    @Column(name = "document_status")
    private String documentStatus;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "del_flg")
    private Boolean delFlg = false;
}