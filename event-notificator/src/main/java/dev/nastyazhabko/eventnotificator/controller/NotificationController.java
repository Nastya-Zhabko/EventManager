package dev.nastyazhabko.eventnotificator.controller;

import dev.nastyazhabko.eventnotificator.converter.NotificationConverter;
import dev.nastyazhabko.eventnotificator.dto.NotificationDto;
import dev.nastyazhabko.eventnotificator.security.AuthenticationService;
import dev.nastyazhabko.eventnotificator.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;
    private final NotificationConverter notificationConverter;

    public NotificationController(NotificationService notificationService, AuthenticationService authenticationService, NotificationConverter notificationConverter) {
        this.notificationService = notificationService;
        this.authenticationService = authenticationService;
        this.notificationConverter = notificationConverter;
    }

    @GetMapping
    public List<NotificationDto> getNotifications() {
        log.info("Get request for get user notifications ");
        return notificationService.getAllUserNotifications(
                        authenticationService.getUserIdFromJwtToken())
                .stream()
                .map(notificationConverter::toDto)
                .toList();
    }

    @PostMapping
    public List<Integer> markNotificationAsRead() {
        log.info("Get request for mark notification as read");
        return notificationService.markAllNotificationsAsRead(authenticationService.getUserIdFromJwtToken());
    }
}
