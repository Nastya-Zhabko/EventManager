package dev.nastyazhabko.eventmanager.event.dto;

import dev.nastyazhabko.eventmanager.event.enums.EventStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Event(
        Integer id,
        String name,
        Integer ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        OffsetDateTime date,
        BigDecimal cost,
        Integer duration,
        Integer locationId,
        EventStatus status
) {
}
