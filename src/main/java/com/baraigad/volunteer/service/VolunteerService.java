package com.baraigad.volunteer.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.volunteer.dto.VolunteerDto;
import com.baraigad.volunteer.entity.Volunteer;

import java.util.List;

public interface VolunteerService {

    VolunteerDto createVolunteer(VolunteerDto volunteerDto);

    VolunteerDto getVolunteerById(Integer volunteerId);
    VolunteerDto getVolunteerByMobile(String volunteerMobile);
    PagedResponseDto<VolunteerDto> getAllVolunteers(
            Integer pageNo,
            Integer pageSize);

    VolunteerDto updateVolunteer(Integer volunteerId,
                                 VolunteerDto volunteerDto);

    VolunteerDto updatePassportPhoto(Integer volunteerId, String photoUrl);

    void deleteVolunteer(Integer volunteerId);

    List<Volunteer> findTodaysBirthdays();
}
