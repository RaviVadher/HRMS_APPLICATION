package com.roima.hrms.achievement.service;

import com.roima.hrms.achievement.entity.PostMedia;
import com.roima.hrms.achievement.entity.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    @Value("${file.upload.dir:uploads/posts}")
    private String uploadDir;

    @Value("${file.upload.max-size:52428800}")  // 50MB default
    private long maxFileSize;

    private static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
        "image/jpeg", "image/png", "image/gif", "image/webp"
    ));

    private static final Set<String> ALLOWED_VIDEO_TYPES = new HashSet<>(Arrays.asList(
        "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo", "video/webm"
    ));

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
        // Images
        "jpg", "jpeg", "png", "gif", "webp",
        // Videos
        "mp4", "avi", "mov", "mkv", "flv", "webm", "mpeg", "3gp"
    ));

    private static final long MAX_IMAGE_SIZE = 10_000_000;   // 10MB for images
    private static final long MAX_VIDEO_SIZE = 500_000_000;  // 500MB for videos

    /**
     * Upload media file for achievement post
     * @param file MultipartFile to upload
     * @param postId ID of the post
     * @param userId ID of the user uploading
     * @return PostMedia entity with file information
     */
    public PostMedia uploadMediaFile(MultipartFile file, Long postId, Long userId) throws IOException {

        // Validate file
        validateFile(file);

        // Determine media type
        MediaType mediaType = determineMediaType(file.getContentType());

        // Generate unique filename
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());

        // Create upload directory if doesn't exist
        String uploadPath = uploadDir + "/" + postId;
        Files.createDirectories(Paths.get(uploadPath));

        // Save file to disk
        Path filePath = Paths.get(uploadPath, uniqueFileName);
        file.transferTo(filePath.toFile());

        log.info("File uploaded successfully: {} at {}", file.getOriginalFilename(), filePath);

        // Create PostMedia entity
        PostMedia media = new PostMedia();
        media.setFileName(file.getOriginalFilename());
        media.setFilePath(uploadPath + "/" + uniqueFileName);  // Relative path
        media.setFileSize(file.getSize());
        media.setMimeType(file.getContentType());
        media.setMediaType(mediaType);
        media.setUploadedById(userId);
        media.setUploadedAt(LocalDateTime.now());
        media.setIsDeleted(false);

        return media;
    }

    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            throw new IOException("File size exceeds maximum allowed: " + maxFileSize + " bytes");
        }

        // Check file extension
        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IOException("File type not allowed: " + extension);
        }

        // Validate MIME type and size based on file type
        String contentType = file.getContentType();
        if (isImageFile(contentType)) {
            if (file.getSize() > MAX_IMAGE_SIZE) {
                throw new IOException("Image file size exceeds 10MB limit");
            }
            if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
                throw new IOException("Invalid image MIME type: " + contentType);
            }
        } else if (isVideoFile(contentType)) {
            if (file.getSize() > MAX_VIDEO_SIZE) {
                throw new IOException("Video file size exceeds 500MB limit");
            }
            if (!ALLOWED_VIDEO_TYPES.contains(contentType)) {
                throw new IOException("Invalid video MIME type: " + contentType);
            }
        } else {
            throw new IOException("Unsupported file type: " + contentType);
        }
    }

    /**
     * Determine media type from MIME type
     */
    private MediaType determineMediaType(String contentType) throws IOException {
        if (contentType != null) {
            if (isImageFile(contentType)) {
                return MediaType.IMAGE;
            } else if (isVideoFile(contentType)) {
                return MediaType.VIDEO;
            }
        }
        throw new IOException("Unable to determine media type");
    }

    /**
     * Check if file is image
     */
    private boolean isImageFile(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * Check if file is video
     */
    private boolean isVideoFile(String contentType) {
        return contentType != null && contentType.startsWith("video/");
    }

    /**
     * Get file extension
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Generate unique filename using UUID
     */
    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String timestamp = System.currentTimeMillis() + "";
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "media_" + timestamp + "_" + uuid + "." + extension;
    }

    /**
     * Delete media file
     */
    public void deleteMediaFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
            log.info("File deleted: {}", filePath);
        }
    }

    /**
     * Get file size in human-readable format
     */
    public String getFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    /**
     * Check if file size is within limits
     */
    public boolean isFileSizeValid(long fileSize, MediaType mediaType) {
        if (mediaType == MediaType.IMAGE) {
            return fileSize <= MAX_IMAGE_SIZE;
        } else if (mediaType == MediaType.VIDEO) {
            return fileSize <= MAX_VIDEO_SIZE;
        }
        return false;
    }
}

