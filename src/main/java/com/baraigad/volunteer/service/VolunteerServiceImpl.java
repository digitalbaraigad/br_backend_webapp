package com.baraigad.volunteer.service;

import com.baraigad.adminuser.exception.AdminUserNotFoundException;
import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.volunteer.dto.VolunteerDto;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.volunteer.mapper.VolunteerMapper;
import com.baraigad.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Override
    public VolunteerDto createVolunteer(VolunteerDto volunteerDto) {
        String mobileNumber = normalizeMobileNumber(volunteerDto.getVolunteerMobile());
        volunteerDto.setVolunteerMobile(mobileNumber);

        if (volunteerRepository.findByVolunteerMobile(mobileNumber).isPresent()) {
            throw new DuplicateResourceException(
                    "A volunteer is already registered with this mobile number");
        }

        Volunteer volunteer = VolunteerMapper.mapToEntity(volunteerDto);
        volunteer.setDelFlg(false);

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        return VolunteerMapper.mapToDto(savedVolunteer);
    }

    @Override
    public VolunteerDto getVolunteerById(Integer volunteerId) {

        Volunteer volunteer = volunteerRepository
                .findById(Long.valueOf(volunteerId))
                .orElseThrow(() ->
                        new AdminUserNotFoundException(
                                "Volunteer not found with id : " + volunteerId));

        return VolunteerMapper.mapToDto(volunteer);
    }



    @Override
    public PagedResponseDto<VolunteerDto> getAllVolunteers(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Volunteer> volunteerPage =
                volunteerRepository.findByDelFlgFalse(pageable);

        return PaginationUtil.build(
                volunteerPage,
                VolunteerMapper::mapToDto);
    }

    @Override
    public VolunteerDto updateVolunteer(
            Integer volunteerId,
            VolunteerDto volunteerDto) {

        Volunteer volunteer = volunteerRepository
                .findById(Long.valueOf(volunteerId))
                .orElseThrow(() ->
                        new AdminUserNotFoundException(
                                "Volunteer not found with id : " + volunteerId));

        String mobileNumber = normalizeMobileNumber(volunteerDto.getVolunteerMobile());
        volunteerRepository.findByVolunteerMobile(mobileNumber)
                .filter(existingVolunteer -> !existingVolunteer.getVolunteerId()
                        .equals(volunteerId))
                .ifPresent(existingVolunteer -> {
                    throw new DuplicateResourceException(
                            "A volunteer is already registered with this mobile number");
                });

        volunteer.setVolunteerFName(volunteerDto.getVolunteerFName());
        volunteer.setVolunteerMName(volunteerDto.getVolunteerMName());
        volunteer.setVolunteerLName(volunteerDto.getVolunteerLName());
        volunteer.setVolunteerEmail(volunteerDto.getVolunteerEmail());
        volunteer.setVolunteerMobile(mobileNumber);
        volunteer.setVolunteerCity(volunteerDto.getVolunteerCity());
        volunteer.setVolunteerDistrict(volunteerDto.getVolunteerDistrict());
        volunteer.setVolunteerTaluka(volunteerDto.getVolunteerTaluka());
        volunteer.setVolunteerState(volunteerDto.getVolunteerState());
        volunteer.setVolunteerCountry(volunteerDto.getVolunteerCountry());
        volunteer.setVolunteerPostalCode(volunteerDto.getVolunteerPostalCode());
        volunteer.setVolunteerDOB(volunteerDto.getVolunteerDOB());
        volunteer.setVolunteerOccupation(volunteerDto.getVolunteerOccupation());
        volunteer.setVolunteerAvailability(volunteerDto.getVolunteerAvailability());

        Volunteer updatedVolunteer =
                volunteerRepository.save(volunteer);

        return VolunteerMapper.mapToDto(updatedVolunteer);
    }

    @Override
    public VolunteerDto updatePassportPhoto(Integer volunteerId, String photoUrl) {
        Volunteer volunteer = volunteerRepository
                .findById(Long.valueOf(volunteerId))
                .orElseThrow(() -> new AdminUserNotFoundException(
                        "Volunteer not found with id : " + volunteerId));
        volunteer.setVolunteerPassportPhotoUrl(photoUrl);
        return VolunteerMapper.mapToDto(volunteerRepository.save(volunteer));
    }

    @Override
    public void deleteVolunteer(Integer volunteerId) {

        Volunteer volunteer = volunteerRepository
                .findById(Long.valueOf(volunteerId))
                .orElseThrow(() ->
                        new AdminUserNotFoundException(
                                "Volunteer not found with id : " + volunteerId));

        volunteer.setDelFlg(true);

        volunteerRepository.save(volunteer);
    }

    @Override
    public List<Volunteer> findTodaysBirthdays() {

        LocalDate today = LocalDate.now();

        return volunteerRepository.findByDelFlgFalse()
                .stream()
                .filter(v ->
                        v.getVolunteerDOB() != null
                                && v.getVolunteerDOB().getMonthValue()
                                == today.getMonthValue()
                                && v.getVolunteerDOB().getDayOfMonth()
                                == today.getDayOfMonth())
                .toList();
    }

    @Override
    public VolunteerDto getVolunteerByMobile(String mobileNumber) {

        Volunteer volunteer = volunteerRepository
                .findByVolunteerMobileAndDelFlg(
                        normalizeMobileNumber(mobileNumber),
                        false)
                .orElseThrow(() ->
                        new RuntimeException("Volunteer not found"));

        return VolunteerMapper.mapToDto(volunteer);
    }

    private String normalizeMobileNumber(String mobileNumber) {
        return mobileNumber == null ? null : mobileNumber.trim();
    }

}
