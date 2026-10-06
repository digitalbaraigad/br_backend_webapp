package com.baraigad.gallery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class GalleryMediaStorageService {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp",
            "video/mp4", "mp4", "video/webm", "webm");
    private final Path uploadDirectory;

    public GalleryMediaStorageService(@Value("${baraigad.gallery-media.upload-dir:uploads/gallery}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file, String mediaType) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Please attach a valid image or video.");
        String normalizedType = mediaType == null ? "" : mediaType.trim().toUpperCase();
        long maxBytes = "VIDEO".equals(normalizedType) ? 100L * 1024 * 1024 : 20L * 1024 * 1024;
        if (file.getSize() > maxBytes) throw new IllegalArgumentException(
                "Gallery " + ("VIDEO".equals(normalizedType) ? "video" : "image")
                        + " must be " + ("VIDEO".equals(normalizedType) ? "100" : "20") + " MB or smaller.");
        String mimeType = file.getContentType();
        boolean isImage = mimeType != null && mimeType.startsWith("image/");
        boolean isVideo = mimeType != null && mimeType.startsWith("video/");
        if (("IMAGE".equals(normalizedType) && !isImage) || ("VIDEO".equals(normalizedType) && !isVideo))
            throw new IllegalArgumentException("The attached file must match the selected media type.");
        String extension = EXTENSIONS.get(mimeType);
        if (extension == null) throw new IllegalArgumentException("Only JPG, PNG, WebP, MP4, and WebM files are allowed.");
        try {
            Files.createDirectories(uploadDirectory);
            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadDirectory.resolve(fileName).normalize();
            if (!target.startsWith(uploadDirectory)) throw new IllegalArgumentException("Invalid gallery file name.");
            try (var input = file.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/gallery/" + fileName;
        } catch (IOException error) {
            throw new IllegalStateException("Gallery media could not be stored.", error);
        }
    }
}
