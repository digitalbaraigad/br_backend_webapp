package com.baraigad.activity.dto;

import com.baraigad.volunteer.dto.VolunteerDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VolunteerHistoryResponseDto {

    private VolunteerDto volunteer;

    private Integer totalActivities;

    private List<ActivityHistoryDto> mohims;

    private List<ActivityHistoryDto> sohalas;

    private List<ActivityHistoryDto> events;

    private List<ActivityHistoryDto> otherActivities;

    private List<ActivityHistoryDto> activities;

}
