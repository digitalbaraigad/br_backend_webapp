package com.baraigad.blog.controller;

import com.baraigad.blog.dto.BlogDto;
import com.baraigad.blog.service.BlogService;
import com.baraigad.blog.service.BlogImageStorageService;
import com.baraigad.common.Constants;
import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.BLOG_API)
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final BlogImageStorageService blogImageStorageService;

    @PostMapping
    public ResponseEntity<ApiResponse<BlogDto>>
    createBlog(
            @Valid
            @RequestBody BlogDto dto) {

        return ResponseUtil.created(
                Constants.BLOG_CREATED_SUCCESSFULLY,
                blogService.createBlog(dto));
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogDto>>
    getBlogById(
            @PathVariable Long blogId) {

        return ResponseUtil.success(
                Constants.BLOG_FETCHED_SUCCESSFULLY,
                blogService.getBlogById(blogId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponseDto<BlogDto>>>
    getAllBlogs(
            @RequestParam(defaultValue = Constants.PAGE_NO)
            Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE)
            Integer pageSize) {

        return ResponseUtil.success(
                Constants.BLOG_FETCHED_SUCCESSFULLY,
                blogService.getAllBlogs(
                        pageNo,
                        pageSize));
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogDto>>
    updateBlog(
            @PathVariable Long blogId,
            @Valid
            @RequestBody BlogDto dto) {

        return ResponseUtil.success(
                Constants.BLOG_UPDATED_SUCCESSFULLY,
                blogService.updateBlog(
                        blogId,
                        dto));
    }

    @PostMapping(value = "/{blogId}/cover-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BlogDto>> uploadCoverImage(
            @PathVariable Long blogId,
            @RequestParam("image") MultipartFile image) {
        return ResponseUtil.success(
                "Blog cover image uploaded successfully",
                blogService.updateCoverImage(blogId, blogImageStorageService.store(image)));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ApiResponse<Object>>
    deleteBlog(
            @PathVariable Long blogId) {

        blogService.deleteBlog(blogId);

        return ResponseUtil.success(
                Constants.BLOG_DELETED_SUCCESSFULLY,
                null);
    }
}
