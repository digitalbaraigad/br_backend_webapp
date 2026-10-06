package com.baraigad.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "blog_info",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "blog_title"),
                @UniqueConstraint(columnNames = "blog_slug")
        })
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "blog_title",
            nullable = false)
    private String blogTitle;

    @Column(name = "blog_title_mr")
    private String blogTitleMr;

    @Column(name = "blog_slug",
            nullable = false)
    private String blogSlug;

    @Column(name = "blog_summary",
            length = 1000)
    private String blogSummary;

    @Column(name = "blog_summary_mr", length = 1000)
    private String blogSummaryMr;

    @Column(name = "blog_content",
            columnDefinition = "TEXT")
    private String blogContent;

    @Column(name = "blog_content_mr", columnDefinition = "TEXT")
    private String blogContentMr;

    @Column(name = "blog_author")
    private String blogAuthor;

    @Column(name = "blog_author_mr")
    private String blogAuthorMr;

    @Column(name = "cover_media_id")
    private Long coverMediaId;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "tags")
    private String tags;

    @Column(name = "tags_mr")
    private String tagsMr;

    @Column(name = "category")
    private String category;

    @Column(name = "category_mr")
    private String categoryMr;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "del_flg")
    private Boolean delFlg = false;
}
