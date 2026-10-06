package com.baraigad.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BlogDto {

    private Long blogId;

    @NotBlank(message = "Blog title is required")
    private String blogTitle;

    @NotBlank(message = "Marathi blog title is required")
    private String blogTitleMr;

    @NotBlank(message = "Blog slug is required")
    private String blogSlug;

    private String blogSummary;

    private String blogSummaryMr;

    private String blogContent;

    private String blogContentMr;

    private String blogAuthor;

    private String blogAuthorMr;

    private Long coverMediaId;

    private String coverImageUrl;

    private String tags;

    private String tagsMr;

    private String category;

    private String categoryMr;

    private LocalDate publishDate;

    private String status;

    private LocalDateTime createdDate;
}
