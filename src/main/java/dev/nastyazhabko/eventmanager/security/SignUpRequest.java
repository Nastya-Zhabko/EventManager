package dev.nastyazhabko.eventmanager.security;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        @Size(min = 3)
        String login,
        @NotBlank
        @Size(min = 5)
        String password,
        @NotNull
        @Min(18)
        Integer age
) {
}
