package com.baraigad.volunteer.controller;

import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.volunteer.dto.VolunteerDto;
import com.baraigad.volunteer.service.VolunteerPhotoStorageService;
import com.baraigad.volunteer.service.VolunteerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/** Public endpoints used by the volunteer registration link and QR code. */
@RestController
@RequestMapping("/api/public/volunteer")
@RequiredArgsConstructor
public class PublicVolunteerController {

    private final VolunteerService volunteerService;
    private final VolunteerPhotoStorageService photoStorageService;

    @PostMapping
    public ResponseEntity<?> createVolunteer(@Valid @RequestBody VolunteerDto volunteerDto) {
        try {
            return ResponseUtil.created("Volunteer created successfully", volunteerService.createVolunteer(volunteerDto));
        } catch (DuplicateResourceException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(ex.getMessage())
                            .data(Map.of("exists", true, "volunteer", volunteerService.getVolunteerByMobile(volunteerDto.getVolunteerMobile())))
                            .build());
        }
    }

    @PostMapping(value = "/{id}/passport-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<VolunteerDto>> uploadPassportPhoto(
            @PathVariable Integer id,
            @RequestParam("photo") MultipartFile photo) {
        return ResponseUtil.success(
                "Volunteer passport-size photo uploaded successfully",
                volunteerService.updatePassportPhoto(id, photoStorageService.store(photo)));
    }

    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getVolunteerByMobile(@PathVariable String mobileNumber) {
        try {
            return ResponseUtil.success("Volunteer fetched successfully", Map.of("exists", true, "volunteer", volunteerService.getVolunteerByMobile(mobileNumber)));
        } catch (RuntimeException ex) {
            return ResponseUtil.success("No existing volunteer registration found. You can continue with a new registration.", Map.of("exists", false));
        }
    }
}
