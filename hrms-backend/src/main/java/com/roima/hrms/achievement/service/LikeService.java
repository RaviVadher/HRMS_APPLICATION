package com.roima.hrms.achievement.service;

import com.roima.hrms.achievement.dto.LikeDTO;
import com.roima.hrms.achievement.dto.UserBasicDTO;
import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.achievement.entity.Like;
import com.roima.hrms.achievement.repository.AchievementPostRepository;
import com.roima.hrms.achievement.repository.LikeRepository;
import com.roima.hrms.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class LikeService {


    private final  LikeRepository likeRepository;
    private final AchievementPostRepository postRepository;

    public LikeService(LikeRepository likeRepository, AchievementPostRepository postRepository) {
        this.likeRepository=likeRepository;
        this.postRepository=postRepository;
    }

    /**
     * Like a post
     * @param postId ID of post to like
     * @param currentUser User liking the post
     * @return LikeDTO with like details
     * @throws RuntimeException if post not found or already liked
     */
    public LikeDTO likePost(Long postId, User currentUser) {
        log.info("User {} liking post {}", currentUser.getId(), postId);
        // Check if post exists and not deleted
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        if (post.getIsDeleted()) {
            throw new RuntimeException("Cannot like a deleted post");
        }
        // Check if user already liked this post
        if (likeRepository.existsByPostAndUser(post, currentUser)) {
            throw new RuntimeException("You have already liked this post");
        }
        // Create and save like
        Like like = new Like();
        like.setPost(post);
        like.setUser(currentUser);
        like.setCreatedAt(LocalDateTime.now());
        Like savedLike = likeRepository.save(like);
        log.info("Post {} liked by user {}. Like ID: {}", postId, currentUser.getId(), savedLike.getLikeId());
        return mapToDTO(savedLike);
    }

    /**
     * Unlike a post
     * @param postId ID of post to unlike
     * @param currentUser User unliking the post
     * @throws RuntimeException if post not found or not liked
     */
    public void unlikePost(Long postId, User currentUser) {
        log.info("User {} unliking post {}", currentUser.getId(), postId);
        // Check if post exists
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        // Check if user has liked this post
        if (!likeRepository.existsByPostAndUser(post, currentUser)) {
            throw new RuntimeException("You have not liked this post");
        }
        // Delete the like
        likeRepository.deleteByPostAndUser(post, currentUser);
        log.info("Post {} unliked by user {}", postId, currentUser.getId());
    }

    /**
     * Get all users who liked a post
     * @param postId ID of post
     * @return List of UserBasicDTO who liked the post
     * @throws RuntimeException if post not found
     */
    public List<UserBasicDTO> getPostLikers(Long postId) {
        log.info("Fetching likers for post {}", postId);
        // Check if post exists
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        List<User> likers = likeRepository.findUsersByPost(post);
        log.info("Found {} likers for post {}", likers.size(), postId);
        return likers.stream()
                .map(this::mapUserToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if user has liked a post
     * @param postId ID of post
     * @param currentUser User to check
     * @return true if user has liked the post
     */
    public boolean hasUserLikedPost(Long postId, User currentUser) {

        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        return likeRepository.existsByPostAndUser(post, currentUser);
    }

    /**
     * Map Like entity to DTO
     */
    private LikeDTO mapToDTO(Like like) {
        LikeDTO dto = new LikeDTO();
        dto.setLikeId(like.getLikeId());
        dto.setPostId(like.getPost().getPostId());
        dto.setUser(mapUserToDTO(like.getUser()));
        dto.setCreatedAt(like.getCreatedAt());
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

