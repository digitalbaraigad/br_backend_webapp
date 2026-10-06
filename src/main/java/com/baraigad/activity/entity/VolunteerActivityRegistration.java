package com.baraigad.activity.entity;

import com.baraigad.volunteer.entity.Volunteer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "volunteer_activity_registration")
public class VolunteerActivityRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ActivityMaster activity;

    private String remarks;

    private String attendanceStatus;

    private LocalDateTime registrationDate;

    private Boolean delFlg = false;

    @PrePersist
    public void prePersist() {
        registrationDate = LocalDateTime.now();
    }
}