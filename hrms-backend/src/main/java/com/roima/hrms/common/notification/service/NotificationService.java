package com.roima.hrms.common.notification.service;

import com.roima.hrms.common.notification.dto.NotificationResponseDto;
import com.roima.hrms.common.notification.entity.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {

    public void crateNotification(Long userId, String msg, NotificationType type, Long refId,Boolean isRead);
    public List<NotificationResponseDto> getAllNotification(Long userId);
    public void readNotification(Long notificationId);


}
