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
public class LikeDTO {

    private Long likeId;
    private Long postId;
    private UserBasicDTO user;
    private LocalDateTime createdAt;
}

