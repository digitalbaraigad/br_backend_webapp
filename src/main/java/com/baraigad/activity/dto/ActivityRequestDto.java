package com.baraigad.activity.dto;


import com.baraigad.activity.entity.ActivityType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityRequestDto {

    private String activityName;
    private String activityDescription;
    private ActivityType activityType;
    private String activityLocation;
    private LocalDate startDate;
    private LocalDate endDate;
}