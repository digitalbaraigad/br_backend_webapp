package com.baraigad.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentDocumentDto {

    private Long documentId;

    private String documentTitle;

    private String documentType;

    private String documentUrl;
}