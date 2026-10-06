package com.baraigad.activity.repository;

import com.baraigad.activity.entity.ActivityMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMasterRepository
        extends JpaRepository<ActivityMaster, Long> {

}