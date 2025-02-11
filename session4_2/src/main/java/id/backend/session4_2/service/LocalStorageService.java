package id.backend.session4_2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class LocalStorageService {
    // @Value("${file.upload-dir:uploads/members}")
    private final String uploadDir;

    private final String[] ALLOWED_CONTENT_TYPES = {
            "image/jpeg",
            "image/png",
            "image/gif"
    };

    private final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public LocalStorageService(@Value("${file.upload-dir:uploads/members}") String uploadDir) {
        this.uploadDir = uploadDir;
        createUploadDirectory();
    }

    @PostConstruct
    private void createUploadDirectory() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        validateFile(file);

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        Path filePath = Paths.get(uploadDir, fileName);

        // Save file
        Files.copy(file.getInputStream(), filePath);

        // Return relative path
        return "/images/" + fileName;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 5MB");
        }

        String contentType = file.getContentType();
        System.out.println("Received Content-Type: " + contentType); // DEBUGGING LINE
        // If contentType is 'application/octet-stream', determine it from file
        // extension
        if ("application/octet-stream".equals(contentType)) {
            try {
                Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
                file.transferTo(tempFile.toFile());
                contentType = Files.probeContentType(tempFile);
                Files.delete(tempFile); // Clean up temp file
            } catch (IOException e) {
                throw new RuntimeException("Error processing file: " + e.getMessage(), e);
            }
        }

        if (!Arrays.asList(ALLOWED_CONTENT_TYPES).contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG, PNG and GIF are allowed");
        }
    }

    public void deleteFile(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                String fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                Path filePath = Paths.get(uploadDir, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
            }
        }
    }
}