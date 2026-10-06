package com.baraigad.fort.entity;

import com.baraigad.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fort extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fort_id")
    private Long fortId;

    @Column(name = "fort_name", nullable = false)
    private String fortName;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "elevation")
    private Double elevation;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(name = "description_mr", columnDefinition = "TEXT")
    private String descriptionMr;

    @Column(name = "fort_title_en", columnDefinition = "TEXT")
    private String fortTitleEn;

    @Column(name = "fort_title_mr", columnDefinition = "TEXT")
    private String fortTitleMr;

    @Column(name = "heading_en", columnDefinition = "TEXT")
    private String headingEn;

    @Column(name = "heading_mr", columnDefinition = "TEXT")
    private String headingMr;

    @Column(name = "sub_heading_en", columnDefinition = "TEXT")
    private String subHeadingEn;

    @Column(name = "sub_heading_mr", columnDefinition = "TEXT")
    private String subHeadingMr;

    @Column(name = "about_heading_en", columnDefinition = "TEXT")
    private String aboutHeadingEn;

    @Column(name = "about_heading_mr", columnDefinition = "TEXT")
    private String aboutHeadingMr;

    @Column(name = "fort_type_en")
    private String fortTypeEn;

    @Column(name = "fort_type_mr")
    private String fortTypeMr;

    @Column(name = "conservation_status_en")
    private String conservationStatusEn;

    @Column(name = "conservation_status_mr")
    private String conservationStatusMr;

    @Column(name = "cover_media_id")
    private Long coverMediaId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "fort_media_urls", joinColumns = @JoinColumn(name = "fort_id"))
    @OrderColumn(name = "display_order")
    @Column(name = "media_url", length = 2048)
    private List<String> mediaUrls = new ArrayList<>();

    @Column(name = "status")
    private Boolean status;
}
