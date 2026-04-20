package dev.nastyazhabko.eventmanager.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.nastyazhabko.eventmanager.event.enums.EventStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventDto(
        Integer id,
        String name,
        Integer ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        OffsetDateTime date,
        BigDecimal cost,
        Integer duration,
        Integer locationId,
        EventStatus status
) {
}
