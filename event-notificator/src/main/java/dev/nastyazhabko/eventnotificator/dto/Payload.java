package dev.nastyazhabko.eventnotificator.dto;

import java.util.UUID;

public record Payload(
        Integer id,
        UUID messageId,
        String payload
) {
}
