package dev.nastyazhabko.eventmanager.user.dto;

import dev.nastyazhabko.eventmanager.user.enums.UserRole;

public record User(
        Integer id,
        String login,
        Integer age,
        UserRole role
) {
}
