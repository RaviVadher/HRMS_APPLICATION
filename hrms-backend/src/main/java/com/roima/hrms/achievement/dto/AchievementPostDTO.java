package com.roima.hrms.achievement.dto;

import com.roima.hrms.achievement.entity.PostType;
import com.roima.hrms.achievement.entity.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AchievementPostDTO {

    private Long postId;
    private String title;
    private String description;
    private String tags;
    private PostVisibility visibility;
    private PostType postType;
    private UserBasicDTO author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private Long likeCount;
    private Long commentCount;
    private Boolean isLikedByCurrentUser;
    private List<UserBasicDTO> recentLikers;
    private List<CommentDTO> comments;
    private Boolean isSystemGenerated;
    private List<PostMediaDTO> media;
}
