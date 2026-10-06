package com.baraigad.activity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class ActivityHistoryDto {

    private Long activityId;

    private String activityName;

    private String activityType;

    private LocalDate activityDate;

    private String attendanceStatus;
}