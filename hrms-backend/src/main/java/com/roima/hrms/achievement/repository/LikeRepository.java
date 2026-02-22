package com.roima.hrms.achievement.repository;

import com.roima.hrms.achievement.entity.Like;
import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Check if a user has liked a specific post
     */
    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.post = :post AND l.user = :user")
    boolean existsByPostAndUser(@Param("post") AchievementPost post, @Param("user") User user);

    /**
     * Find a like by post and user
     */
    Optional<Like> findByPostAndUser(AchievementPost post, User user);

    /**
     * Get all likes for a specific post
     */
    @Query("SELECT l FROM Like l WHERE l.post = :post ORDER BY l.createdAt DESC")
    List<Like> findByPost(@Param("post") AchievementPost post);

    /**
     * Get all likes by a specific user
     */
    @Query("SELECT l FROM Like l WHERE l.user = :user ORDER BY l.createdAt DESC")
    List<Like> findByUser(@Param("user") User user);

    /**
     * Get count of likes for a post
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
    long countByPost(@Param("post") AchievementPost post);

    /**
     * Get all users who liked a post
     */
    @Query("SELECT l.user FROM Like l WHERE l.post = :post ORDER BY l.createdAt DESC")
    List<User> findUsersByPost(@Param("post") AchievementPost post);

    /**
     * Delete like by post and user
     */
    void deleteByPostAndUser(AchievementPost post, User user);
}

