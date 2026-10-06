package com.baraigad.blog.repository;

import com.baraigad.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository
        extends JpaRepository<Blog, Long> {

    Optional<Blog> findByBlogIdAndDelFlgFalse(
            Long blogId);

    Page<Blog> findByDelFlgFalse(
            Pageable pageable);

    boolean existsByBlogTitleAndDelFlgFalse(
            String blogTitle);

    boolean existsByBlogSlugAndDelFlgFalse(
            String blogSlug);

    Optional<Blog> findByBlogTitleAndDelFlgFalse(
            String blogTitle);

    Optional<Blog> findByBlogSlugAndDelFlgFalse(
            String blogSlug);
    Long countByDelFlgFalse();
    List<Blog>
    findTop3ByDelFlgFalseAndStatusOrderByPublishDateDesc(
            String status);
}