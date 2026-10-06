package com.baraigad.participant.repository;

import com.baraigad.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByMobileNumber(String mobileNumber);
}
