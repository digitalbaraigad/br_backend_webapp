package com.baraigad.participant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @Column(name = "participant_name", nullable = false)
    private String name;

    @Column(name = "participant_surname", nullable = false)
    private String surname;

    @Column(name = "participant_dob", nullable = false)
    private LocalDate dob;

    @Column(name = "participant_mobile", nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "taluka")
    private String taluka;

    @Column(name = "email_id", nullable = false)
    private String emailId;

    @Column(name = "interested_activity", nullable = false)
    private String interestedActivity;

    // This is deliberately a value link, not a foreign key: a participant may
    // register before becoming a volunteer. It always contains the participant
    // mobile and matches volunteer_info.volunteer_mobile after they join.
    @Column(name = "volunteer_mobile", length = 10)
    private String volunteerMobile;
}
