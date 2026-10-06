package com.baraigad.activity.service;

import com.baraigad.activity.dto.ActivityRegistrationRequestDto;
import com.baraigad.activity.dto.VolunteerHistoryResponseDto;

public interface VolunteerActivityService {

    void registerVolunteer(ActivityRegistrationRequestDto request);

    VolunteerHistoryResponseDto getVolunteerHistory(
            String mobileNumber);
}