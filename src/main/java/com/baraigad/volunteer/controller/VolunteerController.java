package com.baraigad.volunteer.controller;

import com.baraigad.common.Constants;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.volunteer.dto.VolunteerDto;
import com.baraigad.volunteer.service.VolunteerPhotoStorageService;
import com.baraigad.volunteer.service.VolunteerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.VOLUNTEER_API)
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;
    private final VolunteerPhotoStorageService photoStorageService;

    @PostMapping
    public ResponseEntity<ApiResponse<VolunteerDto>> createVolunteer(
            @Valid @RequestBody VolunteerDto volunteerDto) {

        return ResponseUtil.created(
                Constants.VOLUNTEERS_CREATED_SUCCESSFULLY,
                volunteerService.createVolunteer(volunteerDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerDto>> getVolunteerById(
            @PathVariable Integer id) {

        return ResponseUtil.success(
                Constants.VOLUNTEERS_FETCHED_SUCCESSFULLY,
                volunteerService.getVolunteerById(id));
    }


   @GetMapping
   public ResponseEntity<
           ApiResponse<PagedResponseDto<VolunteerDto>>>
   getAllVolunteers(

           @RequestParam(defaultValue = Constants.PAGE_NO)
           Integer pageNo,
           @RequestParam(defaultValue = Constants.PAGE_SIZE)
           Integer pageSize) {

       return ResponseUtil.success(
               Constants.VOLUNTEERS_RETRIEVED_SUCCESSFULLY,
               volunteerService.getAllVolunteers(
                       pageNo,
                       pageSize));
   }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerDto>> updateVolunteer(
            @PathVariable Integer id,
            @Valid @RequestBody VolunteerDto volunteerDto) {

        return ResponseUtil.success(
                Constants.VOLUNTEERS_UPDATED_SUCCESSFULLY,
                volunteerService.updateVolunteer(id, volunteerDto));
    }

    @PostMapping(value = "/{id}/passport-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<VolunteerDto>> uploadPassportPhoto(
            @PathVariable Integer id,
            @RequestParam("photo") MultipartFile photo) {
        return ResponseUtil.success(
                "Volunteer passport-size photo uploaded successfully",
                volunteerService.updatePassportPhoto(id, photoStorageService.store(photo)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVolunteer(
            @PathVariable Integer id) {

        volunteerService.deleteVolunteer(id);

        return ResponseUtil.success(
                Constants.VOLUNTEERS_DELETED_SUCCESSFULLY,
                Constants.SUCCESS);
    }
    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<ApiResponse<VolunteerDto>> getVolunteerByMobile(
            @PathVariable String mobileNumber) {

        return ResponseUtil.success(
                Constants.VOLUNTEERS_FETCHED_SUCCESSFULLY,
                volunteerService.getVolunteerByMobile(mobileNumber));
    }
}
