package dev.nastyazhabko.eventmanager.location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
        Integer id,
        @NotBlank(message = "Название должно быть заполнено")
        String name,
        @NotBlank(message = "Адрес должен быть заполнен")
        String address,
        @Min(value = 5, message = "Вместимость не может быть меньше 5")
        @NotNull(message = "Вместимость должна быть заполнена")
        Integer capacity,
        String description
) {
}
