package com.baraigad.volunteer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "volunteer_info")
public class Volunteer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="volunteer_id")
    private Integer volunteerId;

    @Column(name="volunteer_f_name")
    private String volunteerFName;

    @Column(name="del_flag")
    private boolean delFlg;

    @Column(name="volunteer_m_name")
    private String volunteerMName;
    @Column(name="volunteer_l_name")
    private String volunteerLName;

    @Column(name="volunteer_email")
    private String volunteerEmail;

    @Column(
            name = "volunteer_mobile",
            nullable = false,
            unique = true,
            length = 10
    )
    private String volunteerMobile;

    @Column(name="volunteer_city")
    private String volunteerCity;

    @Column(name="volunteer_district")
    private String volunteerDistrict;

    @Column(name="volunteer_taluka")
    private String volunteerTaluka;

    @Column(name="volunteer_state")
    private String volunteerState;

    @Column(name="volunteer_country")
    private String volunteerCountry;

    @Column(name="volunteer_postal_code")
    private String volunteerPostalCode;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name="volunteer_dob")
    private LocalDate volunteerDOB;

    @Column(name="volunteer_occupation")
    private String volunteerOccupation;

    @Column(name="volunteer_availability")
    private String volunteerAvailability;

    @Column(name="volunteer_passport_photo_url", length = 500)
    private String volunteerPassportPhotoUrl;

}
