package com.baraigad.fort.controller;

import com.baraigad.adminuser.service.AdminUserService;
import com.baraigad.common.Constants;
import com.baraigad.common.response.*;
import com.baraigad.fort.dto.FortDto;
import com.baraigad.fort.entity.Fort;
import com.baraigad.fort.mapper.FortMapper;
import com.baraigad.fort.repository.FortRepository;
import com.baraigad.fort.service.FortService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.FORT_API)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FortController {

    private final FortService fortService;
    private final FortMediaStorageService fortMediaStorageService;

    @PostMapping
    public ResponseEntity<ApiResponse<FortDto>> createFort(
            @RequestBody FortDto fortDto) {

        return ResponseUtil.success(
                Constants.FORT_CREATED_SUCCESSFULLY,
                fortService.createFort(fortDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<FortDto>>> getAllForts(

            @RequestParam(defaultValue = Constants.PAGE_NO)
            Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE)
            Integer pageSize) {

        return ResponseUtil.success(
                Constants.FORTS_RETRIEVED_SUCCESSFULLY,
                fortService.getAllForts(pageNo, pageSize));
    }

    /**
     * Get Fort By FortId
     */
    @GetMapping("/{fortId}")
    public ResponseEntity<ApiResponse<FortDto>> getFortById(
            @PathVariable Long fortId) {

        FortDto fort = fortService.getFortById(fortId);

        return ResponseUtil.success(
                Constants.FORT_RETRIEVED_SUCCESSFULLY,
                fort);
    }



    /**
     * Update Fort
     */
    @PutMapping("/{fortId}")
    public ResponseEntity<ApiResponse<FortDto>> updateFort(
            @PathVariable Long fortId,
            @RequestBody FortDto fortDto) {

        FortDto updatedFort =
                fortService.updateFort(fortId, fortDto);

        return ResponseUtil.success(
                Constants.FORT_UPDATED_SUCCESSFULLY,
                updatedFort);
    }

    @PostMapping(value = "/{fortId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FortDto>> uploadMedia(
            @PathVariable Long fortId,
            @RequestParam("files") List<MultipartFile> files) {
        return ResponseUtil.success("Fort media uploaded successfully",
                fortService.addMediaUrls(fortId, fortMediaStorageService.storeAll(files)));
    }

    // Soft Delete Fort

    @DeleteMapping("/{fortId}")
    public ResponseEntity<ApiResponse<String>> deleteFort(
            @PathVariable Long fortId) {

        fortService.deleteFort(fortId);

        return ResponseUtil.success(
                Constants.FORT_DELETED_SUCCESSFULLY,
                Constants.SUCCESS);
    }
}
