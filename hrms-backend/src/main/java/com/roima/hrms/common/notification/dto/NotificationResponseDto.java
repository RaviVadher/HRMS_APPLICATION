package com.roima.hrms.common.notification.dto;

import com.roima.hrms.common.notification.entity.NotificationType;
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
public class NotificationResponseDto {

    private Long id;
    private String msg;
    private Long refId;
    private Long userId;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;


}
