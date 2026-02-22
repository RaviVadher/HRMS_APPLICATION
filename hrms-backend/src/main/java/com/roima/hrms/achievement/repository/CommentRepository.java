package com.roima.hrms.achievement.repository;

import com.roima.hrms.achievement.entity.Comment;
import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Get all non-deleted comments for a post
     */
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findByPostAndIsDeletedFalse(@Param("post") AchievementPost post);

    /**
     * Get all comments by a user
     */
    @Query("SELECT c FROM Comment c WHERE c.author = :user AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findByAuthorAndIsDeletedFalse(@Param("user") User user);

    /**
     * Get count of active comments for a post
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post AND c.isDeleted = false")
    long countByPostAndIsDeletedFalse(@Param("post") AchievementPost post);

    /**
     * Find comment by ID (only active comments)
     */
    @Query("SELECT c FROM Comment c WHERE c.commentId = :commentId AND c.isDeleted = false")
    java.util.Optional<Comment> findByIdAndIsDeletedFalse(@Param("commentId") Long commentId);

    /**
     * Get all comments (including deleted) for a post
     */
    @Query("SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.createdAt DESC")
    List<Comment> findAllByPost(@Param("post") AchievementPost post);
}

