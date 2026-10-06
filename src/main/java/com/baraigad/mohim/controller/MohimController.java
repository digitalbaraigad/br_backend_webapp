package com.baraigad.mohim.controller;

import com.baraigad.common.Constants;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.mohim.dto.MohimDto;
import com.baraigad.mohim.service.MohimImageStorageService;
import com.baraigad.mohim.service.MohimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(Constants.MOHIM_API)
@RequiredArgsConstructor
public class MohimController {

    private final MohimService mohimService;
    private final MohimImageStorageService mohimImageStorageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MohimDto>>
    createMohim(
            @Valid
            @RequestBody MohimDto dto) {

        return ResponseUtil.created(
                Constants.MOHIM_CREATED_SUCCESSFULLY,
                mohimService.createMohim(dto));
    }

    @GetMapping("/{mohimId}")
    public ResponseEntity<ApiResponse<MohimDto>>
    getMohimById(
            @PathVariable Long mohimId) {

        return ResponseUtil.success(
                Constants.MOHIM_FETCHED_SUCCESSFULLY,
                mohimService.getMohimById(
                        mohimId));
    }

    @GetMapping
    public ResponseEntity<
            ApiResponse<PagedResponseDto<MohimDto>>>
    getAllMohims(

            @RequestParam(defaultValue = Constants.PAGE_NO)
            Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE)
            Integer pageSize) {

        return ResponseUtil.success(
                Constants.MOHIM_FETCHED_SUCCESSFULLY,
                mohimService.getAllMohims(
                        pageNo,
                        pageSize));
    }

    @PutMapping("/{mohimId}")
    public ResponseEntity<ApiResponse<MohimDto>>
    updateMohim(
            @PathVariable Long mohimId,
            @RequestBody MohimDto dto) {

        return ResponseUtil.success(
                Constants.MOHIM_UPDATED_SUCCESSFULLY,
                mohimService.updateMohim(
                        mohimId,
                        dto));
    }

    @PostMapping(value = "/{mohimId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MohimDto>> uploadPhotos(
            @PathVariable Long mohimId,
            @RequestParam("images") List<MultipartFile> images) {
        return ResponseUtil.success(
                "Mohim photos uploaded successfully",
                mohimService.addPhotoUrls(mohimId, mohimImageStorageService.storeAll(images)));
    }

    @DeleteMapping("/{mohimId}")
    public ResponseEntity<ApiResponse<Object>>
    deleteMohim(
            @PathVariable Long mohimId) {

        mohimService.deleteMohim(mohimId);

        return ResponseUtil.success(
                Constants.MOHIM_DELETED_SUCCESSFULLY,
                null);
    }
}
