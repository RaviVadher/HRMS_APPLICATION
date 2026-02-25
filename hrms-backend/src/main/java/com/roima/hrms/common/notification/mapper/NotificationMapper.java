package com.roima.hrms.common.notification.mapper;

import com.roima.hrms.common.notification.dto.NotificationResponseDto;
import com.roima.hrms.common.notification.entity.Notification;

public class NotificationMapper {

    public static NotificationResponseDto toDto(Notification notification) {

        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setMsg(notification.getMsg());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUserId(notification.getUser().getId());

        return dto;
    }
}
