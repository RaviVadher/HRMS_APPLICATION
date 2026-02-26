package com.roima.hrms.achievement.repository;

import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.achievement.enums.PostType;
import com.roima.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AchievementPostRepository extends JpaRepository<AchievementPost, Long> {

    /**
     * Find all active (non-deleted) posts ordered by creation date (newest first)
     */
    @Query("SELECT p FROM AchievementPost p WHERE p.isDeleted = false ORDER BY p.createdAt DESC")
    List<AchievementPost> findAllActivePosts();

    /**
     * Find all active posts by a specific author
     */
    @Query("SELECT p FROM AchievementPost p WHERE p.author = :author AND p.isDeleted = false ORDER BY p.createdAt DESC")
    List<AchievementPost> findByAuthorAndIsDeletedFalse(@Param("author") User author);

    /**
     * Check if a birthday/anniversary post already exists for today
     */
    @Query("SELECT COUNT(p) > 0 FROM AchievementPost p WHERE p.author = :author " +
           "AND p.postType = :postType AND CAST(p.createdAt AS DATE) = :date")
    boolean existsByAuthorAndPostTypeAndCreatedAtDate(
            @Param("author") User author,
            @Param("postType") PostType postType,
            @Param("date") LocalDate date);


    /**
     * Find posts by tag (with search)
     */
    @Query("SELECT p FROM AchievementPost p WHERE p.isDeleted = false AND LOWER(p.tags) LIKE LOWER(CONCAT('%', :tag, '%')) ORDER BY p.createdAt DESC")
    List<AchievementPost> findByTagsContaining(@Param("tag") String tag);

    /**
     * Find posts created between date range
     */
    @Query("SELECT p FROM AchievementPost p WHERE p.isDeleted = false AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<AchievementPost> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find birthday and anniversary posts for today
     */
    @Query("SELECT p FROM AchievementPost p WHERE p.isDeleted = false AND p.postType IN (:types) AND CAST(p.createdAt AS DATE) = CAST(CURRENT_DATE AS DATE) ORDER BY p.createdAt DESC")
    List<AchievementPost> findTodaysCelebrations(@Param("types") List<PostType> types);
}

