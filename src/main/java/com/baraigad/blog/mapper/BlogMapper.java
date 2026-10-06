package com.baraigad.blog.mapper;

import com.baraigad.blog.dto.BlogDto;
import com.baraigad.blog.entity.Blog;

public class BlogMapper {

    private BlogMapper() {
    }

    public static BlogDto mapToDto(
            Blog blog) {

        BlogDto dto = new BlogDto();

        dto.setBlogId(blog.getBlogId());
        dto.setBlogTitle(blog.getBlogTitle());
        dto.setBlogTitleMr(blog.getBlogTitleMr());
        dto.setBlogSlug(blog.getBlogSlug());
        dto.setBlogSummary(blog.getBlogSummary());
        dto.setBlogSummaryMr(blog.getBlogSummaryMr());
        dto.setBlogContent(blog.getBlogContent());
        dto.setBlogContentMr(blog.getBlogContentMr());
        dto.setBlogAuthor(blog.getBlogAuthor());
        dto.setBlogAuthorMr(blog.getBlogAuthorMr());
        dto.setCoverMediaId(blog.getCoverMediaId());
        dto.setCoverImageUrl(blog.getCoverImageUrl());
        dto.setTags(blog.getTags());
        dto.setTagsMr(blog.getTagsMr());
        dto.setCategory(blog.getCategory());
        dto.setCategoryMr(blog.getCategoryMr());
        dto.setPublishDate(blog.getPublishDate());
        dto.setStatus(blog.getStatus());
        dto.setCreatedDate(blog.getCreatedDate());

        return dto;
    }

    public static Blog mapToEntity(
            BlogDto dto) {

        Blog blog = new Blog();

        blog.setBlogId(dto.getBlogId());
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

        return blog;
    }
}
