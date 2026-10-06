package com.baraigad.volunteer.exception;

import com.baraigad.volunteer.dto.VolunteerDto;
import com.baraigad.volunteer.entity.Volunteer;

public class VolunteerMapper {

    private VolunteerMapper(){}

    public static VolunteerDto mapToDto(Volunteer volunteer){

        VolunteerDto dto = new VolunteerDto();

        dto.setVolunteerId(volunteer.getVolunteerId());
        dto.setVolunteerFName(volunteer.getVolunteerFName());
        dto.setVolunteerMName(volunteer.getVolunteerMName());
        dto.setVolunteerLName(volunteer.getVolunteerLName());
        dto.setVolunteerEmail(volunteer.getVolunteerEmail());
        dto.setVolunteerMobile(volunteer.getVolunteerMobile());
        dto.setVolunteerCity(volunteer.getVolunteerCity());
        dto.setVolunteerState(volunteer.getVolunteerState());
        dto.setVolunteerCountry(volunteer.getVolunteerCountry());
        dto.setVolunteerPostalCode(volunteer.getVolunteerPostalCode());
        dto.setVolunteerDOB(volunteer.getVolunteerDOB());
        dto.setVolunteerOccupation(volunteer.getVolunteerOccupation());
        dto.setVolunteerAvailability(volunteer.getVolunteerAvailability());
        dto.setDelFlag(volunteer.isDelFlg());

        return dto;
    }

    public static Volunteer mapToEntity(VolunteerDto dto){

        Volunteer volunteer = new Volunteer();

        volunteer.setVolunteerId(dto.getVolunteerId());
        volunteer.setVolunteerFName(dto.getVolunteerFName());
        volunteer.setVolunteerMName(dto.getVolunteerMName());
        volunteer.setVolunteerLName(dto.getVolunteerLName());
        volunteer.setVolunteerEmail(dto.getVolunteerEmail());
        volunteer.setVolunteerMobile(dto.getVolunteerMobile());
        volunteer.setVolunteerCity(dto.getVolunteerCity());
        volunteer.setVolunteerState(dto.getVolunteerState());
        volunteer.setVolunteerCountry(dto.getVolunteerCountry());
        volunteer.setVolunteerPostalCode(dto.getVolunteerPostalCode());
        volunteer.setVolunteerDOB(dto.getVolunteerDOB());
        volunteer.setVolunteerOccupation(dto.getVolunteerOccupation());
        volunteer.setVolunteerAvailability(dto.getVolunteerAvailability());
        volunteer.setDelFlg(Boolean.TRUE.equals(dto.getDelFlag()));

        return volunteer;
    }
}