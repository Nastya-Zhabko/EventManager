package dev.nastyazhabko.eventmanager.Location;


public record Location(
        Integer id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}
