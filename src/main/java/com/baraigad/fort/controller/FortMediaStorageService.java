package com.baraigad.fort.controller;

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

@Service
public class FortMediaStorageService {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp",
            "video/mp4", "mp4", "video/webm", "webm");
    private final Path uploadDirectory;

    public FortMediaStorageService(@Value("${baraigad.fort-media.upload-dir:uploads/forts}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public List<String> storeAll(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) throw new IllegalArgumentException("Please attach at least one photo or video.");
        return files.stream().map(this::store).toList();
    }

    private String store(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Please attach a valid photo or video.");
        if (file.getSize() > 20L * 1024 * 1024) throw new IllegalArgumentException("Each fort photo or video must be 20 MB or smaller.");
        String extension = EXTENSIONS.get(file.getContentType());
        if (extension == null) throw new IllegalArgumentException("Only JPG, PNG, WebP, MP4, and WebM files are allowed.");
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadDirectory.resolve(fileName).normalize();
            if (!target.startsWith(uploadDirectory)) throw new IllegalArgumentException("Invalid fort media file name.");
            try (var input = file.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/forts/" + fileName;
        } catch (IOException error) {
            throw new IllegalStateException("Fort media could not be stored.", error);
        }
    }
}
