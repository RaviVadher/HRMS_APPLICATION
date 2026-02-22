package com.roima.hrms.achievement.service;

import com.roima.hrms.achievement.dto.AchievementPostDTO;
import com.roima.hrms.achievement.dto.CreatePostRequest;
import com.roima.hrms.achievement.dto.PostMediaDTO;
import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.achievement.entity.PostMedia;
import com.roima.hrms.achievement.entity.PostType;
import com.roima.hrms.achievement.entity.PostVisibility;
import com.roima.hrms.achievement.repository.AchievementPostRepository;
import com.roima.hrms.achievement.repository.PostMediaRepository;
import com.roima.hrms.common.FileStorageService;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AchievementPostService {

    @Autowired
    private AchievementPostRepository postRepository;

    @Autowired
    private PostMediaRepository mediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EmailService emailService;

    /**
     * Create new achievement post with optional media file
     *
     * @param request CreatePostRequest containing post details and optional media file
     * @param currentUser User creating the post (authenticated user)
     * @return AchievementPostDTO with post details and media information
     * @throws IOException if file upload fails
     */
    public AchievementPostDTO createPost(CreatePostRequest request, User currentUser) throws IOException {

        log.info("Creating new achievement post for user: {}", currentUser.getName());

        // Validate request
        validateCreatePostRequest(request);

        // Create AchievementPost entity
        AchievementPost post = new AchievementPost();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setTags(request.getTags());
        post.setVisibility(PostVisibility.valueOf(request.getVisibility()));
        post.setPostType(PostType.ACHIEVEMENT);
        post.setAuthor(currentUser);
        post.setIsDeleted(false);
        post.setCreatedAt(LocalDateTime.now());

        // Save post first to get the ID
        AchievementPost savedPost = postRepository.save(post);
        log.info("Achievement post created with ID: {}", savedPost.getPostId());

        // Handle media file upload if provided
        if (request.getMediaFile() != null && !request.getMediaFile().isEmpty()) {
            try {
                PostMedia media = uploadPostMedia(request.getMediaFile(), savedPost, currentUser);
                log.info("Media file uploaded successfully for post: {}", savedPost.getPostId());
            } catch (IOException e) {
                log.error("Failed to upload media file for post {}: {}", savedPost.getPostId(), e.getMessage());
                // Delete post if media upload fails
                postRepository.delete(savedPost);
                throw new IOException("Failed to upload media file: " + e.getMessage(), e);
            }
        }

        // Convert to DTO and return
        return mapToDTO(savedPost);
    }

    /**
     * Upload and process media file for a post
     *
     * @param mediaFile MultipartFile to upload
     * @param post AchievementPost entity
     * @param user User uploading the file
     * @return PostMedia entity
     * @throws IOException if upload fails
     */
    private PostMedia uploadPostMedia(MultipartFile mediaFile, AchievementPost post, User user) throws IOException {

        log.info("Uploading media file: {} for post: {}", mediaFile.getOriginalFilename(), post.getPostId());

        // Use common FileStorageService to store file
        // Parameters: file, travelId (using postId), userId, docName, moduleName
        String filePath = fileStorageService.store(mediaFile, post.getPostId(), user.getId(), "achievement", "achievement");

        log.info("Media file uploaded successfully to: {}", filePath);

        // Create PostMedia entity
        PostMedia media = new PostMedia();
        media.setFileName(mediaFile.getOriginalFilename());
        media.setFilePath(filePath);
        media.setFileSize(mediaFile.getSize());
        media.setMimeType(mediaFile.getContentType());
        media.setMediaType(determineMediaType(mediaFile.getContentType()));
        media.setUploadedById(user.getId());
        media.setUploadedAt(LocalDateTime.now());
        media.setIsDeleted(false);
        media.setPost(post);

        // Save to database
        PostMedia savedMedia = mediaRepository.save(media);
        log.info("PostMedia record saved with ID: {}", savedMedia.getMediaId());

        return savedMedia;
    }

    /**
     * Determine media type from MIME type
     */
    private com.roima.hrms.achievement.entity.MediaType determineMediaType(String contentType) {
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return com.roima.hrms.achievement.entity.MediaType.IMAGE;
            } else if (contentType.startsWith("video/")) {
                return com.roima.hrms.achievement.entity.MediaType.VIDEO;
            }
        }
        return com.roima.hrms.achievement.entity.MediaType.IMAGE; // default
    }

    /**
     * Validate CreatePostRequest
     *
     * @param request CreatePostRequest to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCreatePostRequest(CreatePostRequest request) {

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (request.getTitle().length() < 3 || request.getTitle().length() > 255) {
            throw new IllegalArgumentException("Title must be between 3 and 255 characters");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        if (request.getDescription().length() < 10 || request.getDescription().length() > 5000) {
            throw new IllegalArgumentException("Description must be between 10 and 5000 characters");
        }

        if (request.getTags() != null && request.getTags().length() > 500) {
            throw new IllegalArgumentException("Tags must not exceed 500 characters");
        }

        if (request.getVisibility() == null || request.getVisibility().trim().isEmpty()) {
            throw new IllegalArgumentException("Visibility must be specified");
        }

        // Validate visibility enum
        try {
            PostVisibility.valueOf(request.getVisibility());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid visibility value. Must be one of: ALL_EMPLOYEES, DEPARTMENT_ONLY, PRIVATE");
        }

        // Validate media file if provided - only image and video allowed
        if (request.getMediaFile() != null && !request.getMediaFile().isEmpty()) {
            validateMediaFile(request.getMediaFile());
        }
    }

    /**
     * Validate media file - only image and video types allowed
     */
    private void validateMediaFile(MultipartFile mediaFile) throws IllegalArgumentException {
        String contentType = mediaFile.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException("Media file type cannot be determined");
        }

        boolean isImage = contentType.startsWith("image/");
        boolean isVideo = contentType.startsWith("video/");

        if (!isImage && !isVideo) {
            throw new IllegalArgumentException("Only image and video files are allowed. Got: " + contentType);
        }

        // Additional validation for specific types
        if (isImage) {
            if (!contentType.matches("image/(jpeg|png|gif|webp)")) {
                throw new IllegalArgumentException("Image must be one of: JPEG, PNG, GIF, WebP");
            }
        }

        if (isVideo) {
            if (!contentType.matches("video/(mp4|mpeg|quicktime|x-msvideo|webm|x-matroska)")) {
                throw new IllegalArgumentException("Video must be one of: MP4, MPEG, MOV, AVI, WebM, MKV");
            }
        }

        // Check file size (50MB max)
        if (mediaFile.getSize() > 52428800) {
            throw new IllegalArgumentException("Media file size exceeds 50MB limit");
        }
    }

    /**
     * Map AchievementPost entity to DTO with media information
     *
     * @param post AchievementPost entity
     * @return AchievementPostDTO
     */
    public AchievementPostDTO mapToDTO(AchievementPost post) {

        AchievementPostDTO dto = new AchievementPostDTO();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setTags(post.getTags());
        dto.setVisibility(post.getVisibility());
        dto.setPostType(post.getPostType());
        dto.setAuthor(mapUserToDTO(post.getAuthor()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setIsDeleted(post.getIsDeleted());
        dto.setDeletedAt(post.getDeletedAt());
        dto.setIsSystemGenerated(post.getIsSystemGenerated());

        // Set engagement counts
        Long likeCount = post.getLikes() != null ? (long) post.getLikes().size() : 0L;
        Long commentCount = post.getComments() != null ? (long) post.getComments().stream()
                .filter(c -> !c.getIsDeleted())
                .count() : 0L;

        dto.setLikeCount(likeCount);
        dto.setCommentCount(commentCount);
        // Default: not known at service layer; UI/controller may override based on current user
        dto.setIsLikedByCurrentUser(false);

        // Set recent likers (last 5)
        if (post.getLikes() != null && !post.getLikes().isEmpty()) {
            List<com.roima.hrms.achievement.dto.UserBasicDTO> recentLikers = post.getLikes().stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .limit(5)
                    .map(like -> mapUserToDTO(like.getUser()))
                    .collect(Collectors.toList());
            dto.setRecentLikers(recentLikers);
        } else {
            dto.setRecentLikers(new ArrayList<>());
        }

        // Set comments
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            List<com.roima.hrms.achievement.dto.CommentDTO> commentDTOs = post.getComments().stream()
                    .filter(c -> !c.getIsDeleted())
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .map(this::mapCommentToDTO)
                    .collect(Collectors.toList());
            dto.setComments(commentDTOs);
        } else {
            dto.setComments(new ArrayList<>());
        }

        // Set media information
        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            List<PostMediaDTO> mediaDTOs = post.getMedia().stream()
                    .filter(m -> !m.getIsDeleted())
                    .map(this::mapMediaToDTO)
                    .collect(Collectors.toList());
            dto.setMedia(mediaDTOs);
        } else {
            dto.setMedia(new ArrayList<>());
        }

        return dto;
    }

    /**
     * Map Comment entity to DTO
     *
     * @param comment Comment entity
     * @return CommentDTO
     */
    private com.roima.hrms.achievement.dto.CommentDTO mapCommentToDTO(com.roima.hrms.achievement.entity.Comment comment) {

        com.roima.hrms.achievement.dto.CommentDTO dto = new com.roima.hrms.achievement.dto.CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setText(comment.getText());
        dto.setAuthor(mapUserToDTO(comment.getAuthor()));
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        return dto;
    }

    /**
     * Map PostMedia entity to DTO
     *
     * @param media PostMedia entity
     * @return PostMediaDTO
     */
    private PostMediaDTO mapMediaToDTO(PostMedia media) {

        PostMediaDTO dto = new PostMediaDTO();
        dto.setMediaId(media.getMediaId());
        dto.setPostId(media.getPost().getPostId());
        dto.setMediaType(media.getMediaType());
        dto.setFileName(media.getFileName());
        dto.setFilePath(media.getFilePath());
        dto.setFileSize(media.getFileSize());
        dto.setMimeType(media.getMimeType());
        dto.setUploadedAt(media.getUploadedAt());

        // Generate URLs (can be customized based on your backend URL)
        String baseUrl = "/api/achievements/" + media.getPost().getPostId() + "/media/" + media.getMediaId();
        dto.setDownloadUrl(baseUrl + "/download");
        dto.setPreviewUrl(baseUrl + "/preview");

        return dto;
    }

    /**
     * Map User to basic user DTO
     *
     * @param user User entity
     * @return UserBasicDTO
     */
    private com.roima.hrms.achievement.dto.UserBasicDTO mapUserToDTO(User user) {

        com.roima.hrms.achievement.dto.UserBasicDTO dto = new com.roima.hrms.achievement.dto.UserBasicDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDesignation(user.getDesignation());
        dto.setProfileImage(user.getProfile_image());

        return dto;
    }

    /**
     * Get post by ID
     *
     * @param postId Post ID
     * @return AchievementPostDTO
     * @throws RuntimeException if post not found
     */
    public AchievementPostDTO getPostById(Long postId) {

        log.info("Fetching post with ID: {}", postId);

        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        return mapToDTO(post);
    }

    /**
     * Get all active posts (paginated)
     *
     * @param pageNumber Page number (0-indexed)
     * @param pageSize Number of posts per page
     * @return List of AchievementPostDTO
     */
    public List<AchievementPostDTO> getAllActivePosts(int pageNumber, int pageSize) {

        log.info("Fetching active posts - Page: {}, Size: {}", pageNumber, pageSize);

        List<AchievementPost> posts = postRepository.findAllActivePosts();

        // Simple pagination logic
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, posts.size());

        return posts.subList(start, end).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete post (soft delete)
     *
     * @param postId Post ID
     * @param currentUser User deleting the post
     */
    public void deletePost(Long postId, User currentUser) {

        log.info("Deleting post with ID: {} by user: {}", postId, currentUser.getName());

        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Check if user is the author or has admin role (optional)
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            // Can implement role-based check here
            // For now, allow only author to delete
            throw new RuntimeException("You do not have permission to delete this post");
        }

        // Soft delete
        post.setIsDeleted(true);
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);

        log.info("Post with ID: {} has been deleted", postId);
    }

    /**
     * HR deletion of post for inappropriate content
     * Sends warning email to post author
     *
     * @param postId Post ID to delete
     * @param hrUser HR user deleting the post
     * @param reason Reason for deletion
     * @throws RuntimeException if post not found
     */
    public void deletePostAsHR(Long postId, User hrUser, String reason) {

        log.info("HR user {} deleting post {} for reason: {}", hrUser.getName(), postId, reason);

        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Get author's email
        String authorEmail = post.getAuthor().getEmail();

        // Soft delete
        post.setIsDeleted(true);
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);

        log.info("Post with ID: {} deleted by HR user: {}", postId, hrUser.getName());

        // Send warning email to author
        sendPostDeletionWarningEmail(post.getAuthor(), hrUser, post, reason);
    }

    /**
     * Send warning email when HR deletes content
     */
    private void sendPostDeletionWarningEmail(User author, User hrUser, AchievementPost post, String reason) {

        try {
            String subject = "Content Moderation Notice - Post Deleted";
            String body = buildPostDeletionEmailBody(author, hrUser, post, reason);
            emailService.sendEmail(author.getEmail(), subject, body);
            log.info("Warning email sent to {} for post deletion", author.getEmail());
        } catch (Exception e) {
            log.error("Failed to send warning email to {}: {}", author.getEmail(), e.getMessage());
            // Don't throw exception - deletion already processed
        }
    }

    /**
     * Build email body for post deletion warning
     */
    private String buildPostDeletionEmailBody(User author, User hrUser, AchievementPost post, String reason) {

        return "Dear " + author.getName() + ",\n\n" +
                "Your achievement post titled \"" + post.getTitle() + "\" has been removed by our moderation team.\n\n" +
                "Reason for deletion: " + reason + "\n\n" +
                "Moderation action taken by: " + hrUser.getName() + "\n" +
                "Date: " + LocalDateTime.now() + "\n\n" +
                "If you believe this is a mistake, please contact HR.\n\n" +
                "Best regards,\n" +
                "HR Team";
    }

    /**
     * Get total count of active posts
     *
     * @return Total number of active posts
     */
    public long getActivePostsCount() {

        List<AchievementPost> allPosts = postRepository.findAllActivePosts();
        return allPosts.size();
    }

    /**
     * Get posts by author
     *
     * @param user Author user
     * @return List of AchievementPostDTO
     */
    public List<AchievementPostDTO> getPostsByAuthor(User user) {

        log.info("Fetching posts for author: {}", user.getName());

        List<AchievementPost> posts = postRepository.findByAuthorAndIsDeletedFalse(user);

        return posts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update achievement post
     *
     * @param postId Post ID to update
     * @param request UpdatePostRequest with new details
     * @param currentUser User updating the post (must be author)
     * @return AchievementPostDTO with updated post details
     * @throws RuntimeException if post not found or user lacks permission
     */
    public AchievementPostDTO updatePost(Long postId, com.roima.hrms.achievement.dto.UpdatePostRequest request, User currentUser) {

        log.info("Updating post with ID: {} by user: {}", postId, currentUser.getName());

        // Fetch the post
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Check if post is deleted
        if (post.getIsDeleted()) {
            throw new RuntimeException("Cannot update a deleted post");
        }

        // Authorization: Only author can update the post
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to update this post");
        }

        // Validate request
        validateUpdatePostRequest(request);

        // Update fields
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setTags(request.getTags());
        post.setVisibility(PostVisibility.valueOf(request.getVisibility()));
        post.setUpdatedAt(LocalDateTime.now());

        // Save updated post
        AchievementPost updatedPost = postRepository.save(post);
        log.info("Post with ID: {} has been updated successfully", postId);

        return mapToDTO(updatedPost);
    }

    /**
     * Validate UpdatePostRequest
     *
     * @param request UpdatePostRequest to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUpdatePostRequest(com.roima.hrms.achievement.dto.UpdatePostRequest request) {

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (request.getTitle().length() < 3 || request.getTitle().length() > 255) {
            throw new IllegalArgumentException("Title must be between 3 and 255 characters");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        if (request.getDescription().length() < 10 || request.getDescription().length() > 5000) {
            throw new IllegalArgumentException("Description must be between 10 and 5000 characters");
        }

        if (request.getTags() != null && request.getTags().length() > 500) {
            throw new IllegalArgumentException("Tags must not exceed 500 characters");
        }

        if (request.getVisibility() == null || request.getVisibility().trim().isEmpty()) {
            throw new IllegalArgumentException("Visibility must be specified");
        }

        // Validate visibility enum
        try {
            PostVisibility.valueOf(request.getVisibility());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid visibility value. Must be one of: ALL_EMPLOYEES, DEPARTMENT_ONLY, PRIVATE");
        }
    }

    /**
     * Search posts by title, description, or tags
     *
     * @param query Search query string
     * @return List of AchievementPostDTO matching the search query
     */
    public List<AchievementPostDTO> searchPosts(String query) {

        log.info("Searching posts with query: {}", query);

        if (query == null || query.trim().isEmpty()) {
            log.warn("Empty search query provided");
            return new ArrayList<>();
        }

        String searchQuery = query.trim().toLowerCase();
        List<AchievementPost> allActivePosts = postRepository.findAllActivePosts();

        // Filter posts by matching title, description, or tags
        List<AchievementPost> searchResults = allActivePosts.stream()
                .filter(post ->
                    post.getTitle().toLowerCase().contains(searchQuery) ||
                    post.getDescription().toLowerCase().contains(searchQuery) ||
                    (post.getTags() != null && post.getTags().toLowerCase().contains(searchQuery))
                )
                .collect(Collectors.toList());

        log.info("Found {} posts matching query: {}", searchResults.size(), query);

        return searchResults.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search posts by tag
     *
     * @param tag Tag to search for
     * @return List of AchievementPostDTO with the specified tag
     */
    public List<AchievementPostDTO> searchPostsByTag(String tag) {

        log.info("Searching posts by tag: {}", tag);

        if (tag == null || tag.trim().isEmpty()) {
            log.warn("Empty tag provided");
            return new ArrayList<>();
        }

        String searchTag = tag.trim();
        List<AchievementPost> tagResults = postRepository.findByTagsContaining(searchTag);

        log.info("Found {} posts with tag: {}", tagResults.size(), tag);

        return tagResults.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search posts by author name
     *
     * @param authorName Name of the author to search for
     * @return List of AchievementPostDTO by the author
     */
    public List<AchievementPostDTO> searchPostsByAuthor(String authorName) {

        log.info("Searching posts by author: {}", authorName);

        if (authorName == null || authorName.trim().isEmpty()) {
            log.warn("Empty author name provided");
            return new ArrayList<>();
        }

        String searchName = authorName.trim().toLowerCase();
        List<AchievementPost> allActivePosts = postRepository.findAllActivePosts();

        // Filter posts by author name (case-insensitive)
        List<AchievementPost> authorResults = allActivePosts.stream()
                .filter(post -> post.getAuthor().getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());

        log.info("Found {} posts by author: {}", authorResults.size(), authorName);

        return authorResults.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Advanced search supporting query (title/description/tags), tag, author name,
     * and optional creation date range (inclusive).
     * Dates should be provided as LocalDate (date-only) and are compared against post.createdAt.
     */
    public List<AchievementPostDTO> searchPostsAdvanced(String query, String tag, String author, java.time.LocalDate startDate, java.time.LocalDate endDate) {

        log.info("Advanced search posts - query: {}, tag: {}, author: {}, startDate: {}, endDate: {}",
                query, tag, author, startDate, endDate);

        List<AchievementPost> posts = postRepository.findAllActivePosts();

        java.util.stream.Stream<AchievementPost> stream = posts.stream();

        if (query != null && !query.trim().isEmpty()) {
            String q = query.trim().toLowerCase();
            stream = stream.filter(p -> p.getTitle().toLowerCase().contains(q)
                    || p.getDescription().toLowerCase().contains(q)
                    || (p.getTags() != null && p.getTags().toLowerCase().contains(q)));
        }

        if (tag != null && !tag.trim().isEmpty()) {
            String t = tag.trim().toLowerCase();
            stream = stream.filter(p -> p.getTags() != null && p.getTags().toLowerCase().contains(t));
        }

        if (author != null && !author.trim().isEmpty()) {
            String a = author.trim().toLowerCase();
            stream = stream.filter(p -> p.getAuthor() != null && p.getAuthor().getName().toLowerCase().contains(a));
        }

        if (startDate != null) {
            java.time.LocalDateTime startDateTime = startDate.atStartOfDay();
            stream = stream.filter(p -> p.getCreatedAt() != null && !p.getCreatedAt().isBefore(startDateTime));
        }

        if (endDate != null) {
            java.time.LocalDateTime endDateTime = endDate.atTime(23,59,59, 999_999_999);
            stream = stream.filter(p -> p.getCreatedAt() != null && !p.getCreatedAt().isAfter(endDateTime));
        }

        List<AchievementPost> results = stream.collect(java.util.stream.Collectors.toList());

        log.info("Advanced search found {} posts", results.size());

        return results.stream().map(this::mapToDTO).collect(java.util.stream.Collectors.toList());
    }

    /**
     * Download media file
     *
     * @param postId Post ID
     * @param mediaId Media ID
     * @return ResponseEntity with file content
     */
    public org.springframework.http.ResponseEntity<?> downloadMedia(Long postId, Long mediaId) {
        log.info("Downloading media {} for post {}", mediaId, postId);

        // Verify post exists and is not deleted
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        // Find media
        PostMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with ID: " + mediaId));

        if (media.getIsDeleted()) {
            throw new RuntimeException("Media has been deleted");
        }

        if (!media.getPost().getPostId().equals(postId)) {
            throw new RuntimeException("Media does not belong to this post");
        }

        try {
            org.springframework.core.io.Resource resource = fileStorageService.load(media.getFilePath());
            String contentType = media.getMimeType() != null ? media.getMimeType() : "application/octet-stream";

            return org.springframework.http.ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header("Content-Disposition", "attachment; filename=\"" + media.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error downloading media {}: {}", mediaId, e.getMessage());
            throw new RuntimeException("Error downloading media: " + e.getMessage());
        }
    }

    /**
     * Preview media file
     *
     * @param postId Post ID
     * @param mediaId Media ID
     * @return ResponseEntity with file content for preview
     */
    public org.springframework.http.ResponseEntity<?> previewMedia(Long postId, Long mediaId) {
        log.info("Previewing media {} for post {}", mediaId, postId);

        // Verify post exists and is not deleted
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        // Find media
        PostMedia media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with ID: " + mediaId));

        if (media.getIsDeleted()) {
            throw new RuntimeException("Media has been deleted");
        }

        if (!media.getPost().getPostId().equals(postId)) {
            throw new RuntimeException("Media does not belong to this post");
        }

        try {
            org.springframework.core.io.Resource resource = fileStorageService.load(media.getFilePath());
            String contentType = media.getMimeType() != null ? media.getMimeType() : "application/octet-stream";

            return org.springframework.http.ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header("Content-Disposition", "inline; filename=\"" + media.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error previewing media {}: {}", mediaId, e.getMessage());
            throw new RuntimeException("Error previewing media: " + e.getMessage());
        }
    }

    /**
     * Get all media files for a post
     *
     * @param postId Post ID
     * @return List of PostMediaDTO
     */
    public List<PostMediaDTO> getPostMedia(Long postId) {
        log.info("Getting media for post {}", postId);

        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        if (post.getMedia() == null || post.getMedia().isEmpty()) {
            return new ArrayList<>();
        }

        return post.getMedia().stream()
                .filter(m -> !m.getIsDeleted())
                .map(this::mapMediaToDTO)
                .collect(Collectors.toList());
    }
}

