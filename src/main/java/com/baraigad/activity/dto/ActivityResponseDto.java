package com.baraigad.activity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityResponseDto {

    private Integer activityId;
    private String activityName;
    private String activityDescription;
    private String activityType;
    private String activityLocation;
}