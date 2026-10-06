package com.baraigad.participant.service;

import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.participant.dto.ParticipantDto;
import com.baraigad.participant.entity.Participant;
import com.baraigad.participant.repository.ParticipantRepository;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final VolunteerRepository volunteerRepository;
    private final ParticipantNotificationService participantNotificationService;

    @Override
    public ParticipantDto createParticipant(ParticipantDto dto) {
        String mobile = dto.getMobileNumber().trim();
        if (participantRepository.existsByMobileNumber(mobile)) {
            throw new DuplicateResourceException("A participant is already registered with this mobile number");
        }

        Volunteer linkedVolunteer = volunteerRepository
                .findByVolunteerMobileAndDelFlgFalse(mobile)
                .orElse(null);
        Participant participant = new Participant();
        participant.setName(dto.getName().trim());
        participant.setSurname(dto.getSurname().trim());
        participant.setDob(dto.getDob());
        participant.setMobileNumber(mobile);
        participant.setDistrict(dto.getDistrict().trim());
        participant.setTaluka(dto.getTaluka().trim());
        participant.setEmailId(dto.getEmailId().trim());
        participant.setInterestedActivity(dto.getInterestedActivity().trim());
        participant.setVolunteerMobile(mobile);

        Participant saved = participantRepository.save(participant);
        participantNotificationService.notifyParticipant(saved);
        return toDto(saved, linkedVolunteer);
    }

    @Override
    public PagedResponseDto<ParticipantDto> getAllParticipants(Integer pageNo, Integer pageSize) {
        int safePageNo = Math.max(1, pageNo == null ? 1 : pageNo);
        int safePageSize = Math.max(1, Math.min(pageSize == null ? 10 : pageSize, 100));
        Pageable pageable = PageRequest.of(
                safePageNo - 1,
                safePageSize,
                Sort.by(Sort.Direction.DESC, "participantId"));
        return PaginationUtil.build(participantRepository.findAll(pageable), participant -> {
                    Volunteer volunteer = volunteerRepository
                            .findByVolunteerMobileAndDelFlgFalse(participant.getMobileNumber())
                            .orElse(null);
                    return toDto(participant, volunteer);
                });
    }

    private ParticipantDto toDto(Participant participant, Volunteer linkedVolunteer) {
        ParticipantDto dto = new ParticipantDto();
        dto.setParticipantId(participant.getParticipantId());
        dto.setName(participant.getName());
        dto.setSurname(participant.getSurname());
        dto.setDob(participant.getDob());
        dto.setMobileNumber(participant.getMobileNumber());
        dto.setDistrict(participant.getDistrict());
        dto.setTaluka(participant.getTaluka());
        dto.setEmailId(participant.getEmailId());
        dto.setInterestedActivity(participant.getInterestedActivity());
        dto.setVolunteerMobile(participant.getVolunteerMobile());
        if (linkedVolunteer != null) dto.setLinkedVolunteerId(linkedVolunteer.getVolunteerId());
        return dto;
    }
}
