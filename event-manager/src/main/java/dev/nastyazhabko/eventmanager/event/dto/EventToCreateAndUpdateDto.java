package dev.nastyazhabko.eventmanager.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventToCreateAndUpdateDto(
        @NotBlank(message = "Укажите название мероприятия")
        String name,
        @NotNull(message = "Укажите максимальную вместимость мероприятия")
        @Min(value = 1, message = "Максимальное количество мест должно быть больше 0")
        Integer maxPlaces,
        @NotNull(message = "Укажите дату начала мероприятия")
        @Future(message = "Укажите дату больше текущей")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        OffsetDateTime date,
        @NotNull(message = "Укажите стоимость мероприятия")
        @Min(value = 0, message = "Стоимость не может быть меньше 0")
        BigDecimal cost,
        @NotNull(message = "Укажите продолжительность мероприятия")
        @Min(value = 30, message = "Продолжительность мероприятия не может быть меньше 30")
        Integer duration,
        @NotNull(message = "Укажите локацию проведения мероприятия")
        Integer locationId
) {
}
