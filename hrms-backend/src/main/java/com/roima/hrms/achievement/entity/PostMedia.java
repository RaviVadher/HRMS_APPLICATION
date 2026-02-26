package com.roima.hrms.achievement.entity;

import com.roima.hrms.achievement.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_media")
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_media_id")
    private Long mediaId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_post_id")
    private AchievementPost post;

    @Column(name = "media_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;  // IMAGE or VIDEO

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;  // Relative path from uploads folder

    @Column(name = "file_size")
    private Long fileSize;  // In bytes

    @Column(name = "mime_type")
    private String mimeType;  // e.g., image/jpeg, video/mp4

    @Column(name = "uploaded_by", nullable = false)
    private Long uploadedById;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}

