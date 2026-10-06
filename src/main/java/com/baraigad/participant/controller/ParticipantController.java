package com.baraigad.participant.controller;

import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.participant.dto.ParticipantDto;
import com.baraigad.participant.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ApiResponse<ParticipantDto>> create(@Valid @RequestBody ParticipantDto participantDto) {
        return ResponseUtil.created("Participant registered successfully", participantService.createParticipant(participantDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<ParticipantDto>>> getAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseUtil.success("Participants fetched successfully", participantService.getAllParticipants(pageNo, pageSize));
    }
}
