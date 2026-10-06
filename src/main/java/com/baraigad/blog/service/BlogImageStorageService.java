package com.baraigad.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/** Stores one optional cover image for each blog outside the application bundle. */
@Service
public class BlogImageStorageService {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp");
    private final Path uploadDirectory;

    public BlogImageStorageService(
            @Value("${baraigad.blog-image.upload-dir:uploads/blogs}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Please attach a blog cover image.");
        }
        if (image.getSize() > 20L * 1024 * 1024) {
            throw new IllegalArgumentException("Blog cover image must be 20 MB or smaller.");
        }
        String extension = resolveExtension(image);
        if (extension == null) {
            throw new IllegalArgumentException("Only JPG, PNG, and WebP blog cover images are allowed.");
        }
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadDirectory.resolve(fileName).normalize();
            if (!target.startsWith(uploadDirectory)) {
                throw new IllegalArgumentException("Invalid blog cover image file name.");
            }
            try (var input = image.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/blogs/" + fileName;
        } catch (IOException error) {
            throw new IllegalStateException("Blog cover image could not be stored.", error);
        }
    }

    private String resolveExtension(MultipartFile image) {
        String extension = EXTENSIONS.get(image.getContentType());
        if (extension != null) return extension;
        String name = image.getOriginalFilename();
        if (name == null) return null;
        String lowerName = name.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) return "jpg";
        if (lowerName.endsWith(".png")) return "png";
        if (lowerName.endsWith(".webp")) return "webp";
        return null;
    }
}
