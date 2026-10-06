package com.baraigad.enquiry.controller;

import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.enquiry.dto.ContactEnquiryDto;
import com.baraigad.enquiry.service.ContactEnquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact-enquiries")
@RequiredArgsConstructor
public class ContactEnquiryController {
    private final ContactEnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContactEnquiryDto>> create(@Valid @RequestBody ContactEnquiryDto dto) {
        return ResponseUtil.created("Enquiry submitted successfully", enquiryService.create(dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<ContactEnquiryDto>>> getAll(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseUtil.success("Contact enquiries fetched successfully", enquiryService.getAll(pageNo, pageSize));
    }
}
