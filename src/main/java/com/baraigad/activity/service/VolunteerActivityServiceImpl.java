package com.baraigad.activity.service;

import com.baraigad.activity.dto.ActivityHistoryDto;
import com.baraigad.activity.dto.ActivityRegistrationRequestDto;
import com.baraigad.activity.dto.VolunteerHistoryResponseDto;
import com.baraigad.activity.entity.ActivityType;
import com.baraigad.activity.entity.ActivityMaster;
import com.baraigad.activity.entity.VolunteerActivityRegistration;
import com.baraigad.activity.repository.ActivityMasterRepository;
import com.baraigad.activity.repository.VolunteerActivityRegistrationRepository;
import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.volunteer.mapper.VolunteerMapper;
import com.baraigad.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerActivityServiceImpl
        implements VolunteerActivityService {

    private final VolunteerRepository volunteerRepository;
    private final ActivityMasterRepository activityRepository;
    private final VolunteerActivityRegistrationRepository registrationRepository;

    @Override
    public void registerVolunteer(
            ActivityRegistrationRequestDto request) {

        Volunteer volunteer =
                volunteerRepository
                        .findByVolunteerMobileAndDelFlg(
                                request.getMobileNumber(),
                                false)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Volunteer not found for this mobile number"));

        ActivityMaster activity =
                activityRepository
                        .findById(request.getActivityId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity not found"));

        boolean alreadyExists =
                registrationRepository
                        .existsByVolunteerVolunteerIdAndActivityActivityId(
                                volunteer.getVolunteerId(),
                                activity.getActivityId());

        if (alreadyExists) {
            throw new DuplicateResourceException(
                    "Volunteer already registered");
        }

        VolunteerActivityRegistration registration =
                new VolunteerActivityRegistration();

        registration.setVolunteer(volunteer);
        registration.setActivity(activity);
        registration.setRemarks(request.getRemarks());

        registrationRepository.save(registration);
    }



    @Override
    public VolunteerHistoryResponseDto getVolunteerHistory(
            String mobileNumber) {

        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                    "Enter a valid 10-digit mobile number");
        }

        Volunteer volunteer =
                volunteerRepository
                        .findByVolunteerMobileAndDelFlg(
                                mobileNumber,
                                false)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Volunteer not found for this mobile number"));

        List<ActivityHistoryDto> activities =
                registrationRepository
                        .findByVolunteerVolunteerMobileAndDelFlg(
                                mobileNumber,
                                false)
                        .stream()
                        .map(reg -> ActivityHistoryDto.builder()
                                .activityId(
                                        reg.getActivity().getActivityId())
                                .activityName(
                                        reg.getActivity().getActivityName())
                                .activityType(
                                        reg.getActivity()
                                                .getActivityType()
                                                .name())
                                .activityDate(
                                        reg.getActivity().getStartDate())
                                .attendanceStatus(
                                        reg.getAttendanceStatus())
                                .build())
                        .toList();

        return VolunteerHistoryResponseDto.builder()
                .volunteer(VolunteerMapper.mapToDto(volunteer))
                .totalActivities(
                        activities.size())
                .mohims(filterActivitiesByType(activities, ActivityType.MOHIM))
                .sohalas(filterActivitiesByType(activities, ActivityType.SOHALA))
                .events(filterActivitiesByType(activities, ActivityType.EVENT))
                .otherActivities(activities.stream()
                        .filter(activity -> !ActivityType.MOHIM.name()
                                .equals(activity.getActivityType()))
                        .filter(activity -> !ActivityType.SOHALA.name()
                                .equals(activity.getActivityType()))
                        .filter(activity -> !ActivityType.EVENT.name()
                                .equals(activity.getActivityType()))
                        .toList())
                .activities(
                        activities)
                .build();
    }

    private List<ActivityHistoryDto> filterActivitiesByType(
            List<ActivityHistoryDto> activities,
            ActivityType activityType) {

        return activities.stream()
                .filter(activity -> activityType.name()
                        .equals(activity.getActivityType()))
                .toList();
    }

}
