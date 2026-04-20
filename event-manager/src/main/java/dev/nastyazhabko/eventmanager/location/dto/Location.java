package dev.nastyazhabko.eventmanager.location.dto;

public record Location(
        Integer id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}
