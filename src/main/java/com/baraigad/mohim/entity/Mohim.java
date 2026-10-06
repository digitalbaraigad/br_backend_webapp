package com.baraigad.mohim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "mohim_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "mohim_name")
        })
public class Mohim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mohim_id")
    private Long mohimId;

    @Column(name = "mohim_name",
            nullable = false)
    private String mohimName;

    @Column(name = "mohim_name_mr")
    private String mohimNameMr;

    @Column(name = "mohim_type")
    private String mohimType;

    @Column(name = "mohim_type_mr")
    private String mohimTypeMr;

    @Column(name = "fort_id")
    private Long fortId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "location_mr")
    private String locationMr;

    @Column(name = "description",
            columnDefinition = "TEXT")
    private String description;

    @Column(name = "description_mr", columnDefinition = "TEXT")
    private String descriptionMr;

    @Column(name = "organizer_name")
    private String organizerName;

    @Column(name = "organizer_name_mr")
    private String organizerNameMr;

    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "start_point_mr")
    private String startPointMr;

    @Column(name = "end_point")
    private String endPoint;

    @Column(name = "end_point_mr")
    private String endPointMr;

    @Column(name = "distance")
    private String distance;

    @Column(name = "duration")
    private String duration;

    @Column(name = "duration_mr")
    private String durationMr;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "difficulty_mr")
    private String difficultyMr;

    @Column(name = "whatsapp_group_link", length = 2048)
    private String whatsappGroupLink;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mohim_photo_urls", joinColumns = @JoinColumn(name = "mohim_id"))
    @OrderColumn(name = "display_order")
    @Column(name = "photo_url", length = 2048)
    private List<String> photoUrls = new ArrayList<>();

    @Column(name = "status")
    private String status;

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

    @Column(name = "activity_id")
    private Long activityId;
}
