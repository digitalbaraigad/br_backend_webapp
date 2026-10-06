package com.baraigad.dynamicform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/** Stores optional public-form header images outside the application bundle. */
@Service
public class FormImageStorageService {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );

    private final Path uploadDirectory;

    public FormImageStorageService(
            @Value("${baraigad.form-image.upload-dir:uploads/forms}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile image) {
        return storeImage(image, "Top image");
    }

    public String storeParticipantImage(MultipartFile image) {
        return storeImage(image, "Participant image");
    }

    private String storeImage(MultipartFile image, String label) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Please attach an image.");
        }
        if (image.getSize() > 5L * 1024 * 1024) throw new IllegalArgumentException(label + " must be 5 MB or smaller.");
        String extension = EXTENSIONS.get(image.getContentType());
        if (extension == null) {
            throw new IllegalArgumentException("Only JPG, PNG, and WebP images are allowed.");
        }
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadDirectory.resolve(fileName).normalize();
            if (!target.startsWith(uploadDirectory)) {
                throw new IllegalArgumentException("Invalid image file name.");
            }
            try (var input = image.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/forms/" + fileName;
        } catch (IOException error) {
            throw new IllegalStateException(label + " could not be stored.", error);
        }
    }

}
