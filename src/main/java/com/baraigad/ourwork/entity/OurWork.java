package com.baraigad.ourwork.entity;

import com.baraigad.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "our_work")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OurWork extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workId;
    @Column(columnDefinition = "TEXT") private String titleEn;
    @Column(columnDefinition = "TEXT") private String titleMr;
    @Column(columnDefinition = "TEXT") private String headingEn;
    @Column(columnDefinition = "TEXT") private String headingMr;
    @Column(columnDefinition = "TEXT") private String subHeadingEn;
    @Column(columnDefinition = "TEXT") private String subHeadingMr;
    @Column(columnDefinition = "TEXT") private String descriptionEn;
    @Column(columnDefinition = "TEXT") private String descriptionMr;
    private String workTypeEn;
    private String workTypeMr;
    private String conservationStatusEn;
    private String conservationStatusMr;
    private Boolean active;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "our_work_media_urls", joinColumns = @JoinColumn(name = "work_id"))
    @OrderColumn(name = "display_order")
    @Column(name = "media_url", length = 2048)
    @Builder.Default private List<String> mediaUrls = new ArrayList<>();
}
