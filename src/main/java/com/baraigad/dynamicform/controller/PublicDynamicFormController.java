package com.baraigad.dynamicform.controller;
import com.baraigad.common.response.*;
import com.baraigad.dynamicform.dto.*;
import com.baraigad.dynamicform.service.DynamicFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/public/forms") @RequiredArgsConstructor
public class PublicDynamicFormController {
    private final DynamicFormService service;
    private final com.baraigad.dynamicform.service.FormImageStorageService formImageStorageService;
    @GetMapping("/{publicId}") public ResponseEntity<ApiResponse<DynamicFormDto>> get(@PathVariable String publicId) { return ResponseUtil.success("Form fetched successfully", service.getPublicForm(publicId)); }
    @GetMapping("/lookup/mobile/{mobileNumber}") public ResponseEntity<ApiResponse<java.util.Map<String, String>>> lookupByMobile(@PathVariable String mobileNumber) { return ResponseUtil.success("Shared form details fetched successfully", service.findSharedFormProfileByMobile(mobileNumber)); }
    @PostMapping("/{publicId}/submissions") public ResponseEntity<ApiResponse<DynamicFormSubmissionDto>> submit(@PathVariable String publicId, @Valid @RequestBody DynamicFormSubmissionRequest request) { return ResponseUtil.created("Form submitted successfully", service.submit(publicId, request)); }
    @PostMapping(value = "/{publicId}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<java.util.Map<String, String>>> uploadImage(@PathVariable String publicId, @RequestParam("image") MultipartFile image) {
        service.getPublicForm(publicId);
        return ResponseUtil.success("Participant image uploaded successfully", java.util.Map.of("imageUrl", formImageStorageService.storeParticipantImage(image)));
    }
}
