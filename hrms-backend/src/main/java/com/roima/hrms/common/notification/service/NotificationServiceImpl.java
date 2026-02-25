package com.roima.hrms.common.notification.service;

import com.roima.hrms.common.notification.dto.NotificationResponseDto;
import com.roima.hrms.common.notification.entity.Notification;
import com.roima.hrms.common.notification.entity.NotificationType;
import com.roima.hrms.common.notification.mapper.NotificationMapper;
import com.roima.hrms.common.notification.repository.NotificationRepository;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class NotificationServiceImpl implements  NotificationService {

    private final NotificationRepository notificationRepository;
    private  final UserRepository userRepository;

    public  NotificationServiceImpl(NotificationRepository notificationRepository,
                                    UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void crateNotification(Long userId, String msg, NotificationType type, Long refId, Boolean isRead) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var notification = new Notification();
        notification.setUser(user);
        notification.setMsg(msg);
        notification.setType(type);
        notification.setRefId(refId);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(isRead);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDto> getAllNotification(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    @Override
    public void readNotification(Long notificationId)
    {
        Notification notification= notificationRepository.findById(notificationId).orElseThrow(()->new RuntimeException("Notification not found"));
         notification.setRead(true);
         notificationRepository.save(notification);
    }

}
