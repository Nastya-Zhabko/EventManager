package dev.nastyazhabko.eventnotificator.dto;

import dev.nastyazhabko.eventcommon.kafka.EventType;

import java.time.OffsetDateTime;

public record NotificationDto(
        Integer id,
        Integer eventId,
        EventType eventType,
        OffsetDateTime createdAt,
        Boolean isRead,
        String message,
        String payload
) {
}
