package com.baraigad.activity.service;

import com.baraigad.activity.dto.ActivityRequestDto;
import com.baraigad.activity.dto.ActivityResponseDto;

import java.util.List;

public interface ActivityMasterService {

    ActivityResponseDto createActivity(
            ActivityRequestDto request);

    List<ActivityResponseDto> getAllActivities();
}