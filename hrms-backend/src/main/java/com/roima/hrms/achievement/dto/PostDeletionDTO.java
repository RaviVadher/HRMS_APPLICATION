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
public class PostDeletionDTO {

    private Long deletionId;
    private Long postId;
    private String postTitle;
    private String postDescription;
    private UserBasicDTO postAuthor;
    private UserBasicDTO deletedBy;
    private String deletionReason;
    private LocalDateTime deletedAt;
}

