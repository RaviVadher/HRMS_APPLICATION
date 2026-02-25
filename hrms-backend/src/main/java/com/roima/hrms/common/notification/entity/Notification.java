package com.roima.hrms.common.notification.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="pk_notification_id")
    private Long Id;

    @NotBlank
    @Column(name="msg")
    private  String msg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @Column(name = "fk_reference_id")
    private Long refId;


    private NotificationType type;

    @Column(name="is_read")
    private boolean isRead;

    private LocalDateTime createdAt;

}
