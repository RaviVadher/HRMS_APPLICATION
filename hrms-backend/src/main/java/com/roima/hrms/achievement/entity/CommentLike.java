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
@Table(name = "comment_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fk_comment_id", "fk_user_id"})
})
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_comment_like_id")
    private Long commentLikeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_comment_id")
    private Comment comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_user_id")
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
