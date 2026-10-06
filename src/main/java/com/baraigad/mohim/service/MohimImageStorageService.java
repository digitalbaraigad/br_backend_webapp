package com.baraigad.mohim.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Stores uploaded Mohim / Event photos outside the deployed application. */
@Service
public class MohimImageStorageService {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp");
    private final Path uploadDirectory;

    public MohimImageStorageService(
            @Value("${baraigad.mohim-image.upload-dir:uploads/mohims}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public List<String> storeAll(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) throw new IllegalArgumentException("Please attach at least one Mohim image.");
        return images.stream().map(this::store).toList();
    }

    private String store(MultipartFile image) {
        if (image == null || image.isEmpty()) throw new IllegalArgumentException("Please attach a valid Mohim image.");
        String extension = resolveExtension(image);
        if (extension == null) throw new IllegalArgumentException("Only JPG, PNG, and WebP Mohim images are allowed.");
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadDirectory.resolve(fileName).normalize();
            if (!target.startsWith(uploadDirectory)) throw new IllegalArgumentException("Invalid Mohim image file name.");
            try (var input = image.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/mohims/" + fileName;
        } catch (IOException error) {
            throw new IllegalStateException("Mohim image could not be stored.", error);
        }
    }

    private String resolveExtension(MultipartFile image) {
        String extension = EXTENSIONS.get(image.getContentType());
        if (extension != null) return extension;

        String originalName = image.getOriginalFilename();
        if (originalName == null) return null;
        String lowerName = originalName.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) return "jpg";
        if (lowerName.endsWith(".png")) return "png";
        if (lowerName.endsWith(".webp")) return "webp";
        return null;
    }
}
