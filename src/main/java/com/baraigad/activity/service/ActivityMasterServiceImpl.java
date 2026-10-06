package com.baraigad.activity.service;

import com.baraigad.activity.dto.ActivityRequestDto;
import com.baraigad.activity.dto.ActivityResponseDto;
import com.baraigad.activity.entity.ActivityMaster;
import com.baraigad.activity.repository.ActivityMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityMasterServiceImpl
        implements ActivityMasterService {

    private final ActivityMasterRepository activityRepository;

    @Override
    public ActivityResponseDto createActivity(
            ActivityRequestDto request) {

        ActivityMaster activity = new ActivityMaster();

        activity.setActivityName(
                request.getActivityName());

        activity.setDescription(
                request.getActivityDescription());

        activity.setActivityType(request.getActivityType());

        activity.setLocation(
                request.getActivityLocation());

        activity.setStartDate(request.getStartDate());

        activity.setEndDate(
                request.getEndDate());

        activity.setDelFlg(false);

        activity = activityRepository.save(activity);

        return ActivityResponseDto.builder()
                .activityId(
                        Math.toIntExact(activity.getActivityId()))
                .activityName(
                        activity.getActivityName())
                .activityDescription(
                        activity.getDescription())
                .activityType(
                        activity.getActivityType().name())
                .activityLocation(
                        activity.getLocation())
                .build();
    }

    @Override
    public List<ActivityResponseDto> getAllActivities() {

        return activityRepository.findAll()
                .stream()
                .map(activity ->
                        ActivityResponseDto.builder()
                                .activityId(
                                        Math.toIntExact(activity.getActivityId()))
                                .activityName(
                                        activity.getActivityName())
                                .activityDescription(
                                        activity.getDescription())
                                .activityType(
                                        activity.getActivityType().name())
                                .activityLocation(
                                        activity.getLocation())
                                .build())
                .toList();
    }
}