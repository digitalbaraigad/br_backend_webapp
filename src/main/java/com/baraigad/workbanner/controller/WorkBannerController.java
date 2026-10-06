package com.baraigad.workbanner.controller;

import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.workbanner.dto.WorkBannerDto;
import com.baraigad.workbanner.service.WorkBannerService;
import com.baraigad.workbanner.service.WorkBannerStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/v1/work-banners") @RequiredArgsConstructor @CrossOrigin(origins = "*")
public class WorkBannerController {
    private final WorkBannerService service; private final WorkBannerStorageService storage;
    @GetMapping("/{pageKey}") public ResponseEntity<ApiResponse<List<WorkBannerDto>>> get(@PathVariable String pageKey) { return ResponseUtil.success("Work banner images fetched successfully", service.get(pageKey)); }
    @PostMapping(value = "/{pageKey}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<WorkBannerDto>>> upload(@PathVariable String pageKey, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        if (files == null || files.isEmpty()) throw new IllegalArgumentException("Select at least one banner image to upload.");
        return ResponseUtil.created("Work banner images uploaded successfully", service.add(pageKey, files.stream().map(storage::store).toList()));
    }
    @PutMapping("/{pageKey}/link")
    public ResponseEntity<ApiResponse<WorkBannerDto>> saveLink(@PathVariable String pageKey, @RequestBody Map<String, String> body) {
        if (!"community".equalsIgnoreCase(pageKey)) throw new IllegalArgumentException("Only the WhatsApp Community Group Link can be saved as a link.");
        return ResponseUtil.success("WhatsApp Community Group Link saved successfully", service.saveCommunityLink(body.get("link")));
    }
    @DeleteMapping("/{bannerId}") public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long bannerId) { service.delete(bannerId); return ResponseUtil.success("Work banner image removed successfully", "SUCCESS"); }
}
