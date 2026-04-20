package dev.nastyazhabko.eventcommon.kafka;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventChangeKafkaMessage(
        UUID messageId,
        EventType eventType,
        Integer eventId,
        String event,
        OffsetDateTime occurredAt,
        Integer ownerId,
        Integer changedById,
        List<Integer> subscribers,
        List<ChangeFiled> changes
) {
}
