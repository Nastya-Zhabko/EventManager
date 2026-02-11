package dev.nastyazhabko.eventmanager.Location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
        Integer id,
        @NotBlank
        String name,
        @NotBlank
        String address,
        @Min(5)
        @NotNull
        Integer capacity,
        String description
) {
}
