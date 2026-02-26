package com.roima.hrms.achievement.repository;

import com.roima.hrms.achievement.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentCommentIdAndUser_Id(Long commentId, Long userId);
    long countByCommentCommentId(Long commentId);
    void deleteByCommentCommentIdAndUser_Id(Long commentId, Long userId);
}