package com.baraigad.blog.service;

import com.baraigad.blog.dto.BlogDto;
import com.baraigad.common.response.PagedResponseDto;

public interface BlogService {

    BlogDto createBlog(
            BlogDto dto);

    BlogDto getBlogById(
            Long blogId);

    PagedResponseDto<BlogDto> getAllBlogs(
            Integer pageNo,
            Integer pageSize);

    BlogDto updateBlog(
            Long blogId,
            BlogDto dto);

    BlogDto updateCoverImage(
            Long blogId,
            String coverImageUrl);

    void deleteBlog(
            Long blogId);
}
