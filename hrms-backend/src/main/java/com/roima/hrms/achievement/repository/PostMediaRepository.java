package com.roima.hrms.achievement.repository;

import com.roima.hrms.achievement.entity.PostMedia;
import com.roima.hrms.achievement.entity.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {

    /**
     * Find all media files for a post (non-deleted)
     */
    @Query("SELECT m FROM PostMedia m WHERE m.post.postId = :postId AND m.isDeleted = false ORDER BY m.uploadedAt DESC")
    List<PostMedia> findByPostId(@Param("postId") Long postId);

    /**
     * Find all image media for a post
     */
    @Query("SELECT m FROM PostMedia m WHERE m.post.postId = :postId AND m.mediaType = :mediaType AND m.isDeleted = false")
    List<PostMedia> findByPostIdAndMediaType(@Param("postId") Long postId, @Param("mediaType") MediaType mediaType);

    /**
     * Find all videos for a post
     */
    @Query("SELECT m FROM PostMedia m WHERE m.post.postId = :postId AND m.mediaType = 'VIDEO' AND m.isDeleted = false")
    List<PostMedia> findVideosByPostId(@Param("postId") Long postId);

    /**
     * Find all images for a post
     */
    @Query("SELECT m FROM PostMedia m WHERE m.post.postId = :postId AND m.mediaType = 'IMAGE' AND m.isDeleted = false")
    List<PostMedia> findImagesByPostId(@Param("postId") Long postId);

    /**
     * Count media files for a post
     */
    @Query("SELECT COUNT(m) FROM PostMedia m WHERE m.post.postId = :postId AND m.isDeleted = false")
    Long countByPostId(@Param("postId") Long postId);

    /**
     * Find media by file path
     */
    PostMedia findByFilePath(String filePath);
}

