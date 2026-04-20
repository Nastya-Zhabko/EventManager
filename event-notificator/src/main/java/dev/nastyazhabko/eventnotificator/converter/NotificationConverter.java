package dev.nastyazhabko.eventnotificator.converter;

import dev.nastyazhabko.eventnotificator.dto.Notification;
import dev.nastyazhabko.eventnotificator.dto.NotificationDto;
import dev.nastyazhabko.eventnotificator.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {
    private final PayloadConverter payloadConverter;

    public NotificationConverter(PayloadConverter payloadConverter) {
        this.payloadConverter = payloadConverter;
    }

    public Notification toDomain(NotificationEntity notification) {
        return new Notification(
                notification.getId(),
                notification.getUserId(),
                notification.getEventId(),
                notification.getEventType(),
                payloadConverter.toDomain(notification.getPayload()),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }

    public NotificationEntity toEntity(Notification notification) {
        return new NotificationEntity(
                notification.id(),
                notification.userId(),
                notification.eventId(),
                notification.eventType(),
                payloadConverter.toEntity(notification.payload()),
                notification.isRead(),
                notification.createdAt()
        );
    }

    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.id(),
                notification.eventId(),
                notification.eventType(),
                notification.createdAt(),
                notification.isRead(),
                "Мероприятие было изменено",
                notification.payload().payload()
        );
    }
}
