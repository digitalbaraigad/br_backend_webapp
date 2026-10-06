package com.baraigad.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentBlogDto {

    private Long blogId;

    private String blogTitle;

    private String blogSlug;

    private String blogAuthor;

    private Long coverMediaId;

    private String publishDate;
}