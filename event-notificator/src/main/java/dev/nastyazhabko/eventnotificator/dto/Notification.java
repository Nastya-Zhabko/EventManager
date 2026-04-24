package dev.nastyazhabko.eventnotificator.dto;

import dev.nastyazhabko.eventcommon.kafka.EventType;

import java.time.OffsetDateTime;

public record Notification(
        Integer id,
        Integer userId,
        Integer eventId,
        EventType eventType,
        Payload payload,
        Boolean isRead,
        OffsetDateTime createdAt
) {
}
