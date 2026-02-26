package com.roima.hrms.achievement.service;

import com.roima.hrms.achievement.dto.CommentDTO;
import com.roima.hrms.achievement.dto.CreateCommentRequest;
import com.roima.hrms.achievement.dto.UpdateCommentRequest;
import com.roima.hrms.achievement.dto.UserBasicDTO;
import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.achievement.entity.Comment;
import com.roima.hrms.achievement.entity.CommentLike;
import com.roima.hrms.achievement.repository.AchievementPostRepository;
import com.roima.hrms.achievement.repository.CommentLikeRepository;
import com.roima.hrms.achievement.repository.CommentRepository;
import com.roima.hrms.auth.model.UserPrincipal;
import com.roima.hrms.mail.EmailService;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final AchievementPostRepository postRepository;
    private final EmailService emailService;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          AchievementPostRepository postRepository,
                          EmailService emailService,
                          CommentLikeRepository commentLikeRepository,
                          UserRepository userRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.emailService = emailService;
        this.commentRepository= commentRepository;
        this.postRepository=postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new comment on a post
     * @param postId ID of post to comment on
     * @param request CreateCommentRequest with comment text
     * @param currentUser User creating the comment
     * @return CommentDTO with comment details
     * @throws RuntimeException if post not found or deleted
     */
    public CommentDTO createComment(Long postId, CreateCommentRequest request, User currentUser) {

        log.info("Creating comment on post {} by user {}", postId, currentUser.getName());
        // Validate request
        validateCreateCommentRequest(request);
        // Check if post exists and not deleted
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Cannot comment on a deleted post");
        }
        // Create comment
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(currentUser);
        comment.setText(request.getText());
        comment.setIsDeleted(false);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created with ID: {} on post: {}", savedComment.getCommentId(), postId);
        return mapToDTO(savedComment);
    }

    /**
     * Get all comments for a post
     * @param postId ID of post
     * @return List of CommentDTO for the post
     * @throws RuntimeException if post not found
     */
    public List<CommentDTO> getPostComments(Long postId) {
        log.info("Fetching comments for post {}", postId);
        // Check if post exists
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        List<Comment> comments = commentRepository.findByPostAndIsDeletedFalse(post);
        log.info("Found {} active comments for post {}", comments.size(), postId);
        return comments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update a comment
     * @param commentId ID of comment to update
     * @param request UpdateCommentRequest with new text
     * @param currentUser User updating the comment
     * @return CommentDTO with updated comment details
     * @throws RuntimeException if comment not found or user not author
     */
    public CommentDTO updateComment(Long commentId, UpdateCommentRequest request, User currentUser) {

        log.info("Updating comment {} by user {}", commentId, currentUser.getName());
        // Validate request
        validateUpdateCommentRequest(request);
        // Fetch comment
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));
        // Check authorization: only author can update
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to update this comment");
        }
        // Update comment
        comment.setText(request.getText());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment {} updated successfully", commentId);
        return mapToDTO(updatedComment);
    }

    /**
     * Delete a comment (soft delete)
     * @param commentId ID of comment to delete
     * @param currentUser User deleting the comment
     * @throws RuntimeException if comment not found or user lacks permission
     */
    public void deleteComment(Long commentId, User currentUser) {

        log.info("Deleting comment {} by user {}", commentId, currentUser.getName());
        // Fetch comment
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to delete this comment");
        }
        // Soft delete
        comment.setIsDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("Comment {} deleted by user {}", commentId, currentUser.getName());
    }

    /**
     * HR deletion of a comment (for inappropriate content). Sends warning email to comment author.
     * @param commentId ID of the comment to delete
     * @param hrUser HR user performing deletion
     * @param reason Reason for deletion (displayed in email)
     */
    public void deleteCommentAsHR(Long commentId, User hrUser, String reason) {

        log.info("HR user {} removing comment {} for reason: {}", hrUser.getName(), commentId, reason);
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

        // Soft delete
        comment.setIsDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("Comment {} deleted by HR user {}", commentId, hrUser.getName());

        // Send warning email to author
        try {
            String subject = "Content Moderation Notice - Comment Removed";
            String body = "Dear " + comment.getAuthor().getName() + ",\n\n" +
                    "Your comment on post \"" + comment.getPost().getTitle() + "\" has been removed by HR.\n\n" +
                    "Reason: " + (reason != null ? reason : "Not specified") + "\n\n" +
                    "If you believe this was a mistake, please contact HR.\n\n" +
                    "Regards,\nHR Team";
            emailService.sendEmail(comment.getAuthor().getEmail(), subject, body);
            log.info("Warning email sent to {} for comment deletion", comment.getAuthor().getEmail());
        } catch (Exception e) {
            log.error("Failed to send moderation email for comment {}: {}", commentId, e.getMessage());
        }
    }

    /**
     * Like a comment. Does nothing if user already liked it.
     */
    public void likeComment(Long commentId, Long userId) {
        boolean alreadyLiked = commentLikeRepository
                .findByCommentCommentIdAndUser_Id(commentId, userId)
                .isPresent();

        if (alreadyLiked) {
            throw new RuntimeException("You have already liked this post");

        }
        // Fetch comment and user entities
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Save new like
        CommentLike like = new CommentLike();
        like.setComment(comment);
        like.setUser(user);
        commentLikeRepository.save(like);
    }

    /**
     * Unlike a comment. Does nothing if user hasn't liked it.
     */
    public void unlikeComment(Long commentId, Long userId) {
        commentLikeRepository.deleteByCommentCommentIdAndUser_Id(commentId, userId);
    }

    /**
     * Get total likes on a comment
     */
    public long getTotalLikes(Long commentId) {
        return commentLikeRepository.countByCommentCommentId(commentId);
    }

    /**
     * Validate CreateCommentRequest
     */
    private void validateCreateCommentRequest(CreateCommentRequest request) {

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }

        if (request.getText().length() < 1 || request.getText().length() > 1000) {
            throw new IllegalArgumentException("Comment must be between 1 and 1000 characters");
        }
    }

    /**
     * Validate UpdateCommentRequest
     */
    private void validateUpdateCommentRequest(UpdateCommentRequest request) {

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }
        if (request.getText().length() < 1 || request.getText().length() > 1000) {
            throw new IllegalArgumentException("Comment must be between 1 and 1000 characters");
        }
    }


    /**
     * Map Comment entity to DTO
     */
    private CommentDTO mapToDTO(Comment comment) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setText(comment.getText());
        dto.setAuthor(mapUserToDTO(comment.getAuthor()));
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setLikeCount(commentLikeRepository.countByCommentCommentId(comment.getCommentId()));
        dto.setIsLikedByCurrentUser(commentLikeRepository.findByCommentCommentIdAndUser_Id(comment.getCommentId(),user.getId()).isPresent());
        return dto;
    }

    /**
     * Map User to basic DTO
     */
    private UserBasicDTO mapUserToDTO(User user) {
        UserBasicDTO dto = new UserBasicDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDesignation(user.getDesignation());
        dto.setProfileImage(user.getProfile_image());
        return dto;
    }

}

