package com.baraigad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    private final String imageLocation;
    private final String mohimImageLocation;
    private final String fortMediaLocation;
    private final String galleryMediaLocation;
    private final String blogImageLocation;
    private final String volunteerPhotoLocation;
    private final String workBannerLocation;

    public StaticResourceConfig(
            @Value("${baraigad.form-image.upload-dir:uploads/forms}") String uploadDirectory,
            @Value("${baraigad.mohim-image.upload-dir:uploads/mohims}") String mohimImageDirectory,
            @Value("${baraigad.fort-media.upload-dir:uploads/forts}") String fortMediaDirectory,
            @Value("${baraigad.gallery-media.upload-dir:uploads/gallery}") String galleryMediaDirectory,
            @Value("${baraigad.blog-image.upload-dir:uploads/blogs}") String blogImageDirectory,
            @Value("${baraigad.volunteer-photo.upload-dir:uploads/volunteers}") String volunteerPhotoDirectory,
            @Value("${baraigad.work-banner.upload-dir:uploads/work-banners}") String workBannerDirectory) {
        this.imageLocation = Path.of(uploadDirectory).toAbsolutePath().normalize().toUri().toString();
        this.mohimImageLocation = Path.of(mohimImageDirectory).toAbsolutePath().normalize().toUri().toString();
        this.fortMediaLocation = Path.of(fortMediaDirectory).toAbsolutePath().normalize().toUri().toString();
        this.galleryMediaLocation = Path.of(galleryMediaDirectory).toAbsolutePath().normalize().toUri().toString();
        this.blogImageLocation = Path.of(blogImageDirectory).toAbsolutePath().normalize().toUri().toString();
        this.volunteerPhotoLocation = Path.of(volunteerPhotoDirectory).toAbsolutePath().normalize().toUri().toString();
        this.workBannerLocation = Path.of(workBannerDirectory).toAbsolutePath().normalize().toUri().toString();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/forms/**")
                .addResourceLocations(imageLocation.endsWith("/") ? imageLocation : imageLocation + "/");
        registry.addResourceHandler("/uploads/mohims/**")
                .addResourceLocations(mohimImageLocation.endsWith("/") ? mohimImageLocation : mohimImageLocation + "/");
        registry.addResourceHandler("/uploads/forts/**")
                .addResourceLocations(fortMediaLocation.endsWith("/") ? fortMediaLocation : fortMediaLocation + "/");
        registry.addResourceHandler("/uploads/gallery/**")
                .addResourceLocations(galleryMediaLocation.endsWith("/") ? galleryMediaLocation : galleryMediaLocation + "/");
        registry.addResourceHandler("/uploads/blogs/**")
                .addResourceLocations(blogImageLocation.endsWith("/") ? blogImageLocation : blogImageLocation + "/");
        registry.addResourceHandler("/uploads/volunteers/**")
                .addResourceLocations(volunteerPhotoLocation.endsWith("/") ? volunteerPhotoLocation : volunteerPhotoLocation + "/");
        registry.addResourceHandler("/uploads/work-banners/**")
                .addResourceLocations(workBannerLocation.endsWith("/") ? workBannerLocation : workBannerLocation + "/");
    }
}
