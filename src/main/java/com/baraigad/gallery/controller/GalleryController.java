package com.baraigad.gallery.controller;

import com.baraigad.common.Constants;
import com.baraigad.common.response.*;
import com.baraigad.gallery.dto.GalleryDto;
import com.baraigad.gallery.service.GalleryMediaStorageService;
import com.baraigad.gallery.service.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.GALLERY_API)
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;
    private final GalleryMediaStorageService galleryMediaStorageService;

    @PostMapping
    public ResponseEntity<ApiResponse<GalleryDto>>
    createGallery(
            @Valid
            @RequestBody GalleryDto dto) {

        return ResponseUtil.created(
                Constants.GALLERY_CREATED_SUCCESSFULLY,
                galleryService.createGallery(
                        dto));
    }

    @GetMapping("/{galleryId}")
    public ResponseEntity<ApiResponse<GalleryDto>>
    getGalleryById(
            @PathVariable Long galleryId) {

        return ResponseUtil.success(
                Constants.GALLERY_FETCHED_SUCCESSFULLY,
                galleryService.getGalleryById(
                        galleryId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<
            PagedResponseDto<GalleryDto>>>
    getAllGalleries(
            @RequestParam(defaultValue = Constants.PAGE_NO)
            Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE)
            Integer pageSize) {

        return ResponseUtil.success(
                Constants.GALLERY_FETCHED_SUCCESSFULLY,
                galleryService.getAllGalleries(
                        pageNo,
                        pageSize));
    }

    @GetMapping("/photo")
    public ResponseEntity<ApiResponse<PagedResponseDto<GalleryDto>>> getPhotoGallery(
            @RequestParam(defaultValue = Constants.PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE) Integer pageSize) {
        return ResponseUtil.success(Constants.GALLERY_FETCHED_SUCCESSFULLY,
                galleryService.getGalleriesByCategory("PHOTO", pageNo, pageSize));
    }

    @GetMapping("/video")
    public ResponseEntity<ApiResponse<PagedResponseDto<GalleryDto>>> getVideoGallery(
            @RequestParam(defaultValue = Constants.PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE) Integer pageSize) {
        return ResponseUtil.success(Constants.GALLERY_FETCHED_SUCCESSFULLY,
                galleryService.getGalleriesByCategory("VIDEO", pageNo, pageSize));
    }

    @GetMapping("/event")
    public ResponseEntity<ApiResponse<PagedResponseDto<GalleryDto>>> getEventGallery(
            @RequestParam(defaultValue = Constants.PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE) Integer pageSize) {
        return ResponseUtil.success(Constants.GALLERY_FETCHED_SUCCESSFULLY,
                galleryService.getGalleriesByCategory("EVENT", pageNo, pageSize));
    }

    @PutMapping("/{galleryId}")
    public ResponseEntity<ApiResponse<GalleryDto>>
    updateGallery(
            @PathVariable Long galleryId,
            @RequestBody GalleryDto dto) {

        return ResponseUtil.success(
                Constants.GALLERY_UPDATED_SUCCESSFULLY,
                galleryService.updateGallery(
                        galleryId,
                        dto));
    }

    @PostMapping(value = "/{galleryId}/media", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<GalleryDto>> uploadGalleryMedia(
            @PathVariable Long galleryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") String mediaType) {
        String mediaUrl = galleryMediaStorageService.store(file, mediaType);
        return ResponseUtil.success("Gallery media uploaded successfully",
                galleryService.addMedia(galleryId, mediaUrl, mediaType));
    }

    @DeleteMapping("/{galleryId}/media")
    public ResponseEntity<ApiResponse<GalleryDto>> removeGalleryMedia(@PathVariable Long galleryId) {
        return ResponseUtil.success("Gallery media removed successfully",
                galleryService.removeMedia(galleryId));
    }

    @DeleteMapping("/{galleryId}/media/{galleryMediaId}")
    public ResponseEntity<ApiResponse<GalleryDto>> removeGalleryMediaItem(
            @PathVariable Long galleryId, @PathVariable Long galleryMediaId) {
        return ResponseUtil.success("Gallery attachment removed successfully",
                galleryService.removeMedia(galleryId, galleryMediaId));
    }

    @DeleteMapping("/{galleryId}")
    public ResponseEntity<ApiResponse<Object>>
    deleteGallery(
            @PathVariable Long galleryId) {

        galleryService.deleteGallery(
                galleryId);

        return ResponseUtil.success(
                Constants.GALLERY_DELETED_SUCCESSFULLY,
                null);
    }
}
