package com.baraigad.volunteer.repository;

import com.baraigad.volunteer.entity.Volunteer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Page<Volunteer> findByDelFlgFalse(Pageable pageable);

    List<Volunteer> findByDelFlgFalse();

    long countByDelFlgFalse();
    Optional<Volunteer> findByVolunteerMobileAndDelFlg(
            String volunteerMobile,
            boolean delFlg);
    Optional<Volunteer> findByVolunteerMobile(String volunteerMobile);
    Optional<Volunteer> findByVolunteerMobileAndDelFlgFalse(String volunteerMobile);

}