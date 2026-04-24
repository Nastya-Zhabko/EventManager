package dev.nastyazhabko.eventmanager.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.nastyazhabko.eventmanager.event.enums.EventStatus;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventSearchFilter(
        String name,
        @Min(value = 1, message = "Укажите минимальное количество мест больше 0")
        Integer placesMin,
        @Min(value = 1, message = "Укажите максимальное количество мест больше 0")
        Integer placesMax,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        OffsetDateTime dateStartAfter,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        OffsetDateTime dateStartBefore,
        @Min(value = 0, message = "Укажите минимальную стоимость больше 0")
        BigDecimal costMin,
        @Min(value = 0, message = "Укажите максимальную стоимость больше 0")
        BigDecimal costMax,
        @Min(value = 0, message = "Укажите минимальную продолжительность больше 0")
        Integer durationMin,
        @Min(value = 0, message = "Укажите максимальную продолжительность больше 0")
        Integer durationMax,
        Integer locationId,
        EventStatus eventStatus
) {
}
