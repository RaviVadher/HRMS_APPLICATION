package com.roima.hrms.achievement.controller;

import com.roima.hrms.achievement.dto.*;
import com.roima.hrms.achievement.service.AchievementPostService;
import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AchievementPostController {

    @Autowired
    private AchievementPostService postService;

    @Autowired
    private com.roima.hrms.achievement.service.LikeService likeService;

    @Autowired
    private com.roima.hrms.achievement.service.CommentService commentService;

    /**
     * Create new achievement post with optional media file
     * @param request CreatePostRequest with post details and optional media
     * @return ApiResponse with created post details
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AchievementPostDTO>> createPost(
            @Valid @ModelAttribute CreatePostRequest request) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         User currentUser = userPrincipal.getUser();
        log.info("POST /api/achievements - Creating new post by user: {}",request.getVisibility() , currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            // Create post
            AchievementPostDTO result = postService.createPost(request, currentUser);
            log.info("Post created successfully with ID: {}", result.getPostId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Post created successfully", result));

        } catch (IllegalArgumentException e) {
            log.warn("Validation error while creating post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Validation error", null,
                            java.util.Collections.singletonList(e.getMessage()), 400));

        } catch (IOException e) {
            log.error("File upload error while creating post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "File upload failed", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));

        } catch (Exception e) {
            log.error("Unexpected error while creating post: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Unexpected error occurred", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get all active posts with pagination
     * @param pageNumber Page number (0-indexed), default 0
     * @param pageSize Number of posts per page, default 10
     * @return ApiResponse with paginated posts
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<AchievementPostDTO>>> getAllPosts(
            @RequestParam(defaultValue = "0", name = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize) {

        log.info("GET /api/achievements - Fetching posts. Page: {}, Size: {}", pageNumber, pageSize);

        try {
            // Validate pagination parameters
            if (pageNumber < 0) {
                pageNumber = 0;
            }
            if (pageSize <= 0 || pageSize > 100) {
                pageSize = 10;
            }

            // Get posts
            List<AchievementPostDTO> posts = postService.getAllActivePosts(pageNumber, pageSize);

            // If user is authenticated, mark posts liked by current user
            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserPrincipal) {
                    User currentUser = ((UserPrincipal) principal).getUser();
                    if (currentUser != null) {
                        for (AchievementPostDTO p : posts) {
                            boolean liked = likeService.hasUserLikedPost(p.getPostId(), currentUser);
                            p.setIsLikedByCurrentUser(liked);
                        }
                    }
                }
            } catch (Exception ignore) {
                // ignore if no authentication available
            }

            long totalElements = postService.getActivePostsCount();
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);

            // Create paginated response
            PaginatedResponse<AchievementPostDTO> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setContent(posts);
            paginatedResponse.setPageNumber(pageNumber);
            paginatedResponse.setPageSize(pageSize);
            paginatedResponse.setTotalElements(totalElements);
            paginatedResponse.setTotalPages(totalPages);
            paginatedResponse.setIsFirstPage(pageNumber == 0);
            paginatedResponse.setIsLastPage(pageNumber >= totalPages - 1);
            paginatedResponse.setHasNextPage(pageNumber < totalPages - 1);
            paginatedResponse.setHasPreviousPage(pageNumber > 0);

            log.info("Retrieved {} posts from page {}", posts.size(), pageNumber);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Posts retrieved successfully", paginatedResponse));

        } catch (Exception e) {
            log.error("Error retrieving posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving posts", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get single post by ID
     * @param postId Post ID
     * @return ApiResponse with post details
     */
    @GetMapping("/{postId:\\d+}")
    public ResponseEntity<ApiResponse<AchievementPostDTO>> getPost(
            @PathVariable Long postId) {

        log.info("GET /api/achievements/{} - Fetching post", postId);

        try {
            // Validate postId
            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            AchievementPostDTO post = postService.getPostById(postId);

            // Mark if current user has liked this post
            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserPrincipal) {
                    User currentUser = ((UserPrincipal) principal).getUser();
                    if (currentUser != null) {
                        boolean liked = likeService.hasUserLikedPost(postId, currentUser);
                        post.setIsLikedByCurrentUser(liked);
                    }
                }
            } catch (Exception ignore) {
                // ignore when unauthenticated
            }

            log.info("Post {} retrieved successfully", postId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Post retrieved successfully", post));

        } catch (RuntimeException e) {
            log.warn("Post not found or deleted - ID: {}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error retrieving post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving post", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Update achievement post
     *
     * @param postId Post ID to update
     * @param request UpdatePostRequest with new details
     * @return ApiResponse with updated post
     */
    @PutMapping("/{postId:\\d+}")
    public ResponseEntity<ApiResponse<AchievementPostDTO>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("PUT /api/achievements/{} - Updating post by user: {}", postId, currentUser.getName());

        try {
            // Validate user is authenticated
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            // Validate postId
            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            // Update post
            AchievementPostDTO result = postService.updatePost(postId, request, currentUser);
            log.info("Post {} updated successfully", postId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Post updated successfully", result));

        } catch (IllegalArgumentException e) {
            log.warn("Validation error while updating post {}: {}", postId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Validation error", null,
                            java.util.Collections.singletonList(e.getMessage()), 400));

        } catch (RuntimeException e) {
            // Check if it's a permission error or not found
            if (e.getMessage().contains("permission")) {
                log.warn("Permission denied for user {} to update post {}", currentUser.getName(), postId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Permission denied", null,
                                java.util.Collections.singletonList(e.getMessage()), 403));
            } else {
                log.warn("Post not found or cannot be updated - ID: {}", postId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 404));
            }

        } catch (Exception e) {
            log.error("Error updating post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating post", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Delete post (soft delete)
     *
     * @param postId Post ID to delete
     * @return ApiResponse with success message
     */
    @DeleteMapping("/{postId:\\d+}")
    public ResponseEntity<ApiResponse<String>> deletePost(
            @PathVariable Long postId) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("DELETE /api/achievements/{} - Deleting post by user: {}", postId, currentUser.getName());

        try {
            // Validate user is authenticated
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            // Validate postId
            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            // Delete post
            postService.deletePost(postId, currentUser);

            log.info("Post {} deleted successfully", postId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Post deleted successfully", null));

        } catch (RuntimeException e) {
            // Check if it's a permission error or not found
            if (e.getMessage().contains("permission")) {
                log.warn("Permission denied for user {} to delete post {}", currentUser.getName(), postId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Permission denied", null,
                                java.util.Collections.singletonList(e.getMessage()), 403));
            } else {
                log.warn("Post not found - ID: {}", postId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 404));
            }

        } catch (Exception e) {
            log.error("Error deleting post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting post", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get posts by authenticated user
     *  currentUser Authenticated user
     * @return ApiResponse with user's posts
     */
    @GetMapping("/user/posts")
    public ResponseEntity<ApiResponse<List<AchievementPostDTO>>> getUserPosts() {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("GET /api/achievements/user/posts - Fetching posts for user: {}");

        try {
            // Validate user is authenticated
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            List<AchievementPostDTO> userPosts = postService.getPostsByAuthor(currentUser);

            log.info("Retrieved {} posts for user {}", userPosts.size(), currentUser.getName());

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User posts retrieved successfully", userPosts));

        } catch (Exception e) {
            log.error("Error retrieving user posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving user posts", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get total count of active posts
     *
     * @return ApiResponse with count
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getPostsCount() {

        log.info("GET /api/achievements/count - Fetching total posts count");

        try {
            long count = postService.getActivePostsCount();

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Posts count retrieved successfully", count));

        } catch (Exception e) {
            log.error("Error retrieving posts count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving posts count", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Search posts by title, description, or tags
     *
     * @param query Search query string (searches in title, description, and tags)
     * @return ApiResponse with matching posts
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AchievementPostDTO>>> searchPosts(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr) {

        log.info("GET /api/achievements/search - Searching posts. Query: {}, Tag: {}, Author: {}, startDate: {}, endDate: {}", query, tag, author, startDateStr, endDateStr);

        try {
            java.time.LocalDate startDate = null;
            java.time.LocalDate endDate = null;

            try {
                if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                    startDate = java.time.LocalDate.parse(startDateStr);
                }
                if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                    endDate = java.time.LocalDate.parse(endDateStr);
                }
            } catch (java.time.format.DateTimeParseException dtpe) {
                log.warn("Invalid date format in search: {} or {}", startDateStr, endDateStr);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid date format", null,
                                java.util.Collections.singletonList("Dates must be in yyyy-MM-dd format"), 400));
            }

            List<AchievementPostDTO> results = postService.searchPostsAdvanced(query, tag, author, startDate, endDate);

            log.info("Search completed. Found {} results", results.size());

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Search results retrieved", results));

        } catch (Exception e) {
            log.error("Error searching posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error searching posts", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    // ==================== LIKE ENDPOINTS ====================

    /**
     * Like a post
     *
     * @param postId Post ID to like
     * @return ApiResponse with like details
     */
    @PostMapping("/{postId:\\d+}/like")
    public ResponseEntity<ApiResponse<LikeDTO>> likePost(@PathVariable Long postId) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("POST /api/achievements/{}/like - User {} liking post", postId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            LikeDTO result = likeService.likePost(postId, currentUser);
            log.info("Post {} liked by user {}", postId, currentUser.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Post liked successfully", result));

        } catch (RuntimeException e) {
            log.warn("Error liking post {}: {}", postId, e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 404));
            } else if (e.getMessage().contains("already liked")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 409));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 400));
            }

        } catch (Exception e) {
            log.error("Error liking post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error liking post", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Unlike a post
     *
     * @param postId Post ID to unlike
     * @return ApiResponse with success message
     */
    @DeleteMapping("/{postId:\\d+}/like")
    public ResponseEntity<ApiResponse<Void>> unlikePost(@PathVariable Long postId) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("DELETE /api/achievements/{}/like - User {} unliking post", postId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            likeService.unlikePost(postId, currentUser);
            log.info("Post {} unliked by user {}", postId, currentUser.getId());

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Post unliked successfully", null));

        } catch (RuntimeException e) {
            log.warn("Error unliking post {}: {}", postId, e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 404));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 400));
            }

        } catch (Exception e) {
            log.error("Error unliking post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error unliking post", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get all users who liked a post
     *
     * @param postId Post ID
     * @return ApiResponse with list of users who liked
     */
    @GetMapping("/{postId:\\d+}/likers")
    public ResponseEntity<ApiResponse<List<UserBasicDTO>>> getPostLikers(@PathVariable Long postId) {

        log.info("GET /api/achievements/{}/likers - Fetching likers", postId);

        try {
            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            List<UserBasicDTO> likers = likeService.getPostLikers(postId);
            log.info("Retrieved {} likers for post {}", likers.size(), postId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Likers retrieved successfully", likers));

        } catch (RuntimeException e) {
            log.warn("Post not found: {}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error retrieving likers for post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving likers", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    // ==================== COMMENT ENDPOINTS ====================

    /**
     * Create a comment on a post
     *
     * @param postId Post ID to comment on
     * @param request CreateCommentRequest with comment text
     * @return ApiResponse with created comment
     */
    @PostMapping("/{postId:\\d+}/comments")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("POST /api/achievements/{}/comments - Creating comment by user {}", postId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            CommentDTO result = commentService.createComment(postId, request, currentUser);
            log.info("Comment created on post {} by user {}", postId, currentUser.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Comment created successfully", result));

        } catch (IllegalArgumentException e) {
            log.warn("Validation error creating comment on post {}: {}", postId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Validation error", null,
                            java.util.Collections.singletonList(e.getMessage()), 400));

        } catch (RuntimeException e) {
            log.warn("Post not found or deleted: {}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error creating comment on post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating comment", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get all comments for a post
     *
     * @param postId Post ID
     * @return ApiResponse with list of comments
     */
    @GetMapping("/{postId:\\d+}/comments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getPostComments(@PathVariable Long postId) {

        log.info("GET /api/achievements/{}/comments - Fetching comments", postId);

        try {
            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            List<CommentDTO> comments = commentService.getPostComments(postId);
            log.info("Retrieved {} comments for post {}", comments.size(), postId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true,"", comments));

        } catch (RuntimeException e) {
            log.warn("Post not found: {}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error retrieving comments for post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving comments", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Update a comment
     *
     * @param postId Post ID (for validation)
     * @param commentId Comment ID to update
     * @param request UpdateCommentRequest with new text
     * @return ApiResponse with updated comment
     */
    @PutMapping("/{postId:\\d+}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("PUT /api/achievements/{}/comments/{} - Updating comment by user {}", postId, commentId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            if (commentId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid comment ID", null,
                                java.util.Collections.singletonList("Comment ID must be positive"), 400));
            }

            CommentDTO result = commentService.updateComment(commentId, request, currentUser);
            log.info("Comment {} updated successfully", commentId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Comment updated successfully", result));

        } catch (IllegalArgumentException e) {
            log.warn("Validation error updating comment {}: {}", commentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Validation error", null,
                            java.util.Collections.singletonList(e.getMessage()), 400));

        } catch (RuntimeException e) {
            log.warn("Error updating comment: {}", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 404));
            } else if (e.getMessage().contains("permission")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 403));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 400));
            }

        } catch (Exception e) {
            log.error("Error updating comment {}: {}", commentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating comment", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Delete a comment
     *
     * @param postId Post ID (for validation)
     * @param commentId Comment ID to delete
     * @return ApiResponse with success message
     */
    @DeleteMapping("/{postId:\\d+}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("DELETE /api/achievements/{}/comments/{} - Deleting comment by user {}", postId, commentId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            if (commentId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid comment ID", null,
                                java.util.Collections.singletonList("Comment ID must be positive"), 400));
            }

            commentService.deleteComment(commentId, currentUser);
            log.info("Comment {} deleted by user {}", commentId, currentUser.getId());

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Comment deleted successfully", null));

        } catch (RuntimeException e) {
            log.warn("Error deleting comment: {}", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 404));
            } else if (e.getMessage().contains("permission")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 403));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, e.getMessage(), null,
                                java.util.Collections.singletonList(e.getMessage()), 400));
            }

        } catch (Exception e) {
            log.error("Error deleting comment {}: {}", commentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting comment", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * HR: Delete post as moderation action and notify author
     * @param postId Post ID
     * @param reason Reason for deletion
     * @return ApiResponse
     */
    @DeleteMapping("/{postId:\\d+}/moderate")
    public ResponseEntity<ApiResponse<Void>> moderateDeletePost(
            @PathVariable Long postId,
            @RequestParam(name = "reason", required = false) String reason) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("DELETE /api/achievements/{}/moderate - HR delete request by {}", postId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            // Check HR role
            if (currentUser.getRole() == null || !"HR".equalsIgnoreCase(currentUser.getRole().getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Permission denied", null,
                                java.util.Collections.singletonList("Only HR can perform this action"), 403));
            }

            postService.deletePostAsHR(postId, currentUser, reason);

            return ResponseEntity.ok(new ApiResponse<>(true, "Post removed by HR", null));

        } catch (RuntimeException e) {
            log.warn("Moderation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));
        } catch (Exception e) {
            log.error("Error moderating post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error moderating post", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * HR: Delete comment as moderation action and notify author
     */
    @DeleteMapping("/{postId:\\d+}/comments/{commentId}/moderate")
    public ResponseEntity<ApiResponse<Void>> moderateDeleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam(name = "reason", required = false) String reason) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userPrincipal.getUser();
        log.info("DELETE /api/achievements/{}/comments/{}/moderate - HR delete comment by {}", postId, commentId, currentUser.getName());

        try {
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "User not authenticated", null,
                                java.util.Collections.singletonList("Authentication required"), 401));
            }

            // Check HR role
            if (currentUser.getRole() == null || !"HR".equalsIgnoreCase(currentUser.getRole().getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Permission denied", null,
                                java.util.Collections.singletonList("Only HR can perform this action"), 403));
            }

            commentService.deleteCommentAsHR(commentId, currentUser, reason);

            return ResponseEntity.ok(new ApiResponse<>(true, "Comment removed by HR", null));

        } catch (RuntimeException e) {
            log.warn("Moderation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));
        } catch (Exception e) {
            log.error("Error moderating comment {}: {}", commentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error moderating comment", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }



    /**
     * Download media file for a post
     *
     * @param postId Post ID
     * @param mediaId Media ID
     * @return ResponseEntity with file content
     */
    @GetMapping("/{postId:\\d+}/media/{mediaId:\\d+}/download")
    public ResponseEntity<?> downloadMedia(
            @PathVariable Long postId,
            @PathVariable Long mediaId) {

        log.info("GET /api/achievements/{}/media/{}/download - Downloading media", postId, mediaId);

        try {
            if (postId <= 0 || mediaId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post or media ID", null,
                                java.util.Collections.singletonList("IDs must be positive"), 400));
            }

            return postService.downloadMedia(postId, mediaId);

        } catch (RuntimeException e) {
            log.warn("Media not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error downloading media {}: {}", mediaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error downloading media", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Preview media file for a post (typically for images)
     *
     * @param postId Post ID
     * @param mediaId Media ID
     * @return ResponseEntity with file content for preview
     */
    @GetMapping("/{postId:\\d+}/media/{mediaId:\\d+}/preview")
    public ResponseEntity<?> previewMedia(
            @PathVariable Long postId,
            @PathVariable Long mediaId) {

        log.info("GET /api/achievements/{}/media/{}/preview - Previewing media", postId, mediaId);

        try {
            if (postId <= 0 || mediaId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post or media ID", null,
                                java.util.Collections.singletonList("IDs must be positive"), 400));
            }

            return postService.previewMedia(postId, mediaId);

        } catch (RuntimeException e) {
            log.warn("Media not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error previewing media {}: {}", mediaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error previewing media", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }

    /**
     * Get all media files for a post
     *
     * @param postId Post ID
     * @return ApiResponse with list of media files
     */
    @GetMapping("/{postId:\\d+}/media")
    public ResponseEntity<ApiResponse<List<PostMediaDTO>>> getPostMedia(@PathVariable Long postId) {

        log.info("GET /api/achievements/{}/media - Fetching media", postId);

        try {
            if (postId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid post ID", null,
                                java.util.Collections.singletonList("Post ID must be positive"), 400));
            }

            List<PostMediaDTO> media = postService.getPostMedia(postId);
            log.info("Retrieved {} media files for post {}", media.size(), postId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Media retrieved successfully", media));

        } catch (RuntimeException e) {
            log.warn("Post not found: {}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null,
                            java.util.Collections.singletonList(e.getMessage()), 404));

        } catch (Exception e) {
            log.error("Error retrieving media for post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving media", null,
                            java.util.Collections.singletonList(e.getMessage()), 500));
        }
    }
}

