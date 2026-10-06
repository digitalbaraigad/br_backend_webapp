package com.baraigad.workbanner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_page_banners")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkBanner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;
    @Column(name = "page_key", nullable = false, length = 40)
    private String pageKey;
    @Column(name = "image_url", nullable = false, length = 2048)
    private String imageUrl;
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
