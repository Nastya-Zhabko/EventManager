package dev.nastyazhabko.eventnotificator.kafka;

import dev.nastyazhabko.eventcommon.kafka.ChangeFiled;

import java.util.List;

public record EventPayload(
        String eventName,
        Integer changedById,
        List<ChangeFiled> changes
) {
}
