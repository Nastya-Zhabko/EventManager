package dev.nastyazhabko.eventmanager.location;

public record Location(
        Integer id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}
