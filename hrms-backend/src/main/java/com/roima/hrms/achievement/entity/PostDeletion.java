package com.roima.hrms.achievement.entity;

import com.roima.hrms.user.entity.User;
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
@Table(name = "post_deletions")
public class PostDeletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_deletion_id")
    private Long deletionId;

    @Column(name = "fk_post_id", nullable = false)
    private Long postId;

    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @Column(name = "post_description", columnDefinition = "TEXT")
    private String postDescription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_post_author_id")
    private User postAuthor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_deleted_by_id")
    private User deletedBy;

    @Column(name = "deletion_reason", columnDefinition = "TEXT")
    private String deletionReason;

    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    @PrePersist
    void onCreate() {
        deletedAt = LocalDateTime.now();
    }
}

