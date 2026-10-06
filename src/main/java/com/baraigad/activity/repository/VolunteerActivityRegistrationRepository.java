package com.baraigad.activity.repository;

import com.baraigad.activity.entity.VolunteerActivityRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerActivityRegistrationRepository
        extends JpaRepository<VolunteerActivityRegistration, Long> {

    List<VolunteerActivityRegistration>
    findByVolunteerVolunteerMobileAndDelFlg(
            String mobileNumber,
            Boolean delFlg);

    boolean existsByVolunteerVolunteerIdAndActivityActivityId(
            Integer volunteerId,
            Long activityId);
}