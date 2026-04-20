package dev.nastyazhabko.eventnotificator.service;

import dev.nastyazhabko.eventnotificator.converter.NotificationConverter;
import dev.nastyazhabko.eventnotificator.dto.Notification;
import dev.nastyazhabko.eventnotificator.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {
    Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;
    private final PayloadService payloadService;

    public NotificationService(NotificationRepository notificationRepository, NotificationConverter notificationConverter, PayloadService payloadService) {
        this.notificationRepository = notificationRepository;
        this.notificationConverter = notificationConverter;
        this.payloadService = payloadService;
    }

    public void saveNotification(Notification notification) {
        log.info("Saving notification: {}", notification);
        notificationRepository.save(notificationConverter.toEntity(notification));
    }

    public List<Notification> getAllUserNotifications(Integer userId) {
        log.info("Getting all user notifications for {}", userId);
        return notificationRepository.findAllByUserId(userId)
                .stream()
                .map(notificationConverter::toDomain)
                .toList();
    }

    public List<Integer> markAllNotificationsAsRead(Integer userId) {
        log.info("Marking all user notifications as read for {}", userId);
        return notificationRepository.markAsReadByUserId(userId);
    }

    @Scheduled(cron = "${notification.clearing}")
    public void deleteNotification() {
        log.info("Deleting notifications older then 7 days");
        Set<Integer> payloads = notificationRepository.deleteReadNotificationByDate(OffsetDateTime.now().plusDays(7));
        log.info("Deleted payloads with out notifications");
        payloads.forEach(payload -> {
            boolean b = notificationRepository.findByPayloadId(payload).isEmpty();
            if (b) {
                payloadService.deletePayloadById(payload);
            }
        });
    }

}
