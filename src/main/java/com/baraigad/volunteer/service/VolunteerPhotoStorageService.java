package com.baraigad.volunteer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/** Stores one optional passport-size photograph for a volunteer. */
@Service
public class VolunteerPhotoStorageService {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp");
    private final Path uploadDirectory;
    private final long maximumSizeBytes;

    public VolunteerPhotoStorageService(
            @Value("${baraigad.volunteer-photo.upload-dir:uploads/volunteers}") String uploadDirectory,
            @Value("${baraigad.volunteer-photo.max-file-size:5MB}") org.springframework.util.unit.DataSize maximumSize) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
        this.maximumSizeBytes = maximumSize.toBytes();
    }

    public String store(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Please attach a passport-size photo.");
        }
        if (photo.getSize() > maximumSizeBytes) {
            throw new IllegalArgumentException("Passport-size photo must be 5 MB or smaller.");
        }
        String extension = resolveExtension(photo);
        if (extension == null) {
            throw new IllegalArgumentException("Only JPG, PNG, and WebP passport-size photos are allowed.");
        }
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadDirectory.resolve(fileName).normalize();
            if (!target.startsWith(uploadDirectory)) {
                throw new IllegalArgumentException("Invalid passport photo file name.");
            }
            try (var input = photo.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/volunteers/" + fileName;
        } catch (IOException error) {
            throw new IllegalStateException("Passport-size photo could not be stored.", error);
        }
    }

    private String resolveExtension(MultipartFile photo) {
        String extension = EXTENSIONS.get(photo.getContentType());
        if (extension != null) return extension;
        String name = photo.getOriginalFilename();
        if (name == null) return null;
        String lowerName = name.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) return "jpg";
        if (lowerName.endsWith(".png")) return "png";
        if (lowerName.endsWith(".webp")) return "webp";
        return null;
    }
}
