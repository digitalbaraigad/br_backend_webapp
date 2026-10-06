package com.baraigad.participant.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.participant.dto.ParticipantDto;

public interface ParticipantService {
    ParticipantDto createParticipant(ParticipantDto participantDto);
    PagedResponseDto<ParticipantDto> getAllParticipants(Integer pageNo, Integer pageSize);
}
