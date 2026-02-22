package com.roima.hrms.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailDTO {

    private Long commentId;
    private String text;
    private UserBasicDTO author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Boolean isLikedByCurrentUser;
    private Boolean canCurrentUserEdit;
    private Boolean canCurrentUserDelete;
}

