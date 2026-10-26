package com.baraigad.bulkcommunication.controller;

import com.baraigad.bulkcommunication.dto.*;
import com.baraigad.bulkcommunication.service.BulkCommunicationService;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bulk-communications")
@RequiredArgsConstructor
public class BulkCommunicationController {
    private final BulkCommunicationService service;

    @GetMapping("/recipients")
    public ResponseEntity<ApiResponse<List<BulkRecipientDto>>> recipients(
            @RequestParam(defaultValue = "ALL") String source,
            @RequestParam(required = false) Long formId) {
        return ResponseUtil.success("Bulk communication recipients fetched successfully", service.recipients(source, formId));
    }

    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<BulkTemplateDto>>> templates() {
        return ResponseUtil.success("Bulk WhatsApp templates fetched successfully", service.templates());
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<BulkDispatchResultDto>> send(@Valid @RequestBody BulkDispatchRequest request) {
        return ResponseUtil.success("Bulk communication processed", service.send(request));
    }
}
