package dev.nastyazhabko.eventmanager.user;

public record User(
        Integer id,
        String login,
        Integer age,
        UserRole role
) {
}
