package com.roima.hrms.achievement.dto;

import com.roima.hrms.achievement.entity.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMediaDTO {

    private Long mediaId;
    private Long postId;
    private MediaType mediaType;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String mimeType;
    private Long uploadedById;
    private LocalDateTime uploadedAt;
    private String downloadUrl;  // URL to download the file
    private String previewUrl;   // URL to preview the file (for images/thumbnails)
}

