package com.baraigad.blog.service;

import com.baraigad.blog.dto.BlogDto;
import com.baraigad.blog.entity.Blog;
import com.baraigad.blog.mapper.BlogMapper;
import com.baraigad.blog.repository.BlogRepository;
import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl
        implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public BlogDto createBlog(
            BlogDto dto) {

        if (blogRepository
                .existsByBlogTitleAndDelFlgFalse(
                        dto.getBlogTitle())) {

            throw new DuplicateResourceException(
                    "Blog title already exists : "
                            + dto.getBlogTitle());
        }

        if (blogRepository
                .existsByBlogSlugAndDelFlgFalse(
                        dto.getBlogSlug())) {

            throw new DuplicateResourceException(
                    "Blog slug already exists : "
                            + dto.getBlogSlug());
        }

        Blog blog =
                BlogMapper.mapToEntity(dto);

        blog.setCreatedDate(
                LocalDateTime.now());

        blog.setDelFlg(false);

        Blog savedBlog =
                blogRepository.save(blog);

        return BlogMapper.mapToDto(savedBlog);
    }

    @Override
    public BlogDto getBlogById(
            Long blogId) {

        Blog blog =
                blogRepository
                        .findByBlogIdAndDelFlgFalse(
                                blogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blog not found"));

        return BlogMapper.mapToDto(blog);
    }

    @Override
    public PagedResponseDto<BlogDto> getAllBlogs(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable =
                PageRequest.of(
                        pageNo - 1,
                        pageSize);

        Page<Blog> blogPage =
                blogRepository
                        .findByDelFlgFalse(
                                pageable);

        return PaginationUtil.build(
                blogPage,
                BlogMapper::mapToDto);
    }

    @Override
    public BlogDto updateBlog(
            Long blogId,
            BlogDto dto) {

        Blog blog =
                blogRepository
                        .findByBlogIdAndDelFlgFalse(
                                blogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blog not found"));

        Blog existingTitle =
                blogRepository
                        .findByBlogTitleAndDelFlgFalse(
                                dto.getBlogTitle())
                        .orElse(null);

        if (existingTitle != null
                && !existingTitle.getBlogId()
                .equals(blogId)) {

            throw new DuplicateResourceException(
                    "Blog title already exists : "
                            + dto.getBlogTitle());
        }

        Blog existingSlug =
                blogRepository
                        .findByBlogSlugAndDelFlgFalse(
                                dto.getBlogSlug())
                        .orElse(null);

        if (existingSlug != null
                && !existingSlug.getBlogId()
                .equals(blogId)) {

            throw new DuplicateResourceException(
                    "Blog slug already exists : "
                            + dto.getBlogSlug());
        }

        blog.setBlogTitle(dto.getBlogTitle());
        blog.setBlogTitleMr(dto.getBlogTitleMr());
        blog.setBlogSlug(dto.getBlogSlug());
        blog.setBlogSummary(dto.getBlogSummary());
        blog.setBlogSummaryMr(dto.getBlogSummaryMr());
        blog.setBlogContent(dto.getBlogContent());
        blog.setBlogContentMr(dto.getBlogContentMr());
        blog.setBlogAuthor(dto.getBlogAuthor());
        blog.setBlogAuthorMr(dto.getBlogAuthorMr());
        blog.setCoverMediaId(dto.getCoverMediaId());
        blog.setCoverImageUrl(dto.getCoverImageUrl());
        blog.setTags(dto.getTags());
        blog.setTagsMr(dto.getTagsMr());
        blog.setCategory(dto.getCategory());
        blog.setCategoryMr(dto.getCategoryMr());
        blog.setPublishDate(dto.getPublishDate());
        blog.setStatus(dto.getStatus());

        blog.setUpdatedDate(
                LocalDateTime.now());

        Blog updatedBlog =
                blogRepository.save(blog);

        return BlogMapper.mapToDto(updatedBlog);
    }

    @Override
    public BlogDto updateCoverImage(
            Long blogId,
            String coverImageUrl) {

        Blog blog = blogRepository
                .findByBlogIdAndDelFlgFalse(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        blog.setCoverImageUrl(coverImageUrl);
        blog.setCoverMediaId(null);
        blog.setUpdatedDate(LocalDateTime.now());
        return BlogMapper.mapToDto(blogRepository.save(blog));
    }

    @Override
    public void deleteBlog(
            Long blogId) {

        Blog blog =
                blogRepository
                        .findByBlogIdAndDelFlgFalse(
                                blogId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blog not found"));

        blog.setDelFlg(true);

        blog.setUpdatedDate(
                LocalDateTime.now());

        blogRepository.save(blog);
    }
}
