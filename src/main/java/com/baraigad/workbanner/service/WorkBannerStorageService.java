package com.baraigad.workbanner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WorkBannerStorageService {
    private static final Logger log = LoggerFactory.getLogger(WorkBannerStorageService.class);
    private final Path directory;
    public WorkBannerStorageService(@Value("${baraigad.work-banner.upload-dir:uploads/work-banners}") String uploadDirectory) {
        directory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Please attach a banner image.");
        if (file.getSize() > 20L * 1024 * 1024) throw new IllegalArgumentException("Each banner image must be 20 MB or smaller.");
        String type = file.getContentType();
        if (!"image/jpeg".equals(type) && !"image/png".equals(type) && !"image/webp".equals(type))
            throw new IllegalArgumentException("Only JPG, PNG, and WebP banner images are allowed.");
        String extension = "image/png".equals(type) ? "png" : "image/webp".equals(type) ? "webp" : "jpg";
        try {
            Files.createDirectories(directory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = directory.resolve(fileName).normalize();
            if (!target.startsWith(directory)) throw new IllegalArgumentException("Invalid banner file name.");
            try (var input = file.getInputStream()) { Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING); }
            return "/uploads/work-banners/" + fileName;
        } catch (IOException exception) { throw new IllegalStateException("Banner image could not be stored.", exception); }
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith("/uploads/work-banners/")) return;
        try {
            String fileName = Path.of(imageUrl).getFileName().toString();
            Path target = directory.resolve(fileName).normalize();
            if (!target.startsWith(directory)) throw new IllegalArgumentException("Invalid banner file name.");
            if (Files.deleteIfExists(target)) log.info("Removed work banner file {}", fileName);
        } catch (IOException exception) {
            log.warn("Could not remove work banner file for {}", imageUrl, exception);
        }
    }
}
