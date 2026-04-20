package dev.nastyazhabko.eventcommon.security;

import dev.nastyazhabko.eventcommon.security.enums.UserRole;

public record User(
        Integer id,
        String login,
        Integer age,
        UserRole role
) {
}
