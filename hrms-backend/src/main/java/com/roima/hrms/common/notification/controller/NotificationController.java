package com.roima.hrms.common.notification.controller;

import com.roima.hrms.common.notification.dto.NotificationResponseDto;
import com.roima.hrms.common.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public List<NotificationResponseDto> getAllNotification(@PathVariable Long userId) {
       return notificationService.getAllNotification(userId);
    }

    @PutMapping("/read/{notificationId}")
    public void changeRead(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
    }
}
