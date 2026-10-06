package com.baraigad.dynamicform.controller;
import com.baraigad.common.response.*;
import com.baraigad.dynamicform.dto.*;
import com.baraigad.dynamicform.service.DynamicFormService;
import com.baraigad.dynamicform.service.FormImageStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController @RequestMapping("/api/v1/dynamic-forms") @RequiredArgsConstructor
public class DynamicFormController {
    private final DynamicFormService service;
    private final FormImageStorageService formImageStorageService;
    @PostMapping public ResponseEntity<ApiResponse<DynamicFormDto>> create(@Valid @RequestBody DynamicFormDto form) { return ResponseUtil.created("Form created successfully", service.create(form)); }
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(@RequestParam("image") MultipartFile image) {
        return ResponseUtil.success("Top image uploaded successfully", Map.of("imageUrl", formImageStorageService.store(image)));
    }
    @GetMapping public ResponseEntity<ApiResponse<PagedResponseDto<DynamicFormDto>>> all(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseUtil.success("Forms fetched successfully", service.getAll(pageNo, pageSize));
    }
    @GetMapping("/{formId}/submissions") public ResponseEntity<ApiResponse<List<DynamicFormSubmissionDto>>> submissions(@PathVariable Long formId) { return ResponseUtil.success("Form submissions fetched successfully", service.getSubmissions(formId)); }
}
