package dev.nastyazhabko.eventmanager.location.converter;

import dev.nastyazhabko.eventmanager.location.dto.Location;
import dev.nastyazhabko.eventmanager.location.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoConverter {
    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(LocationDto location) {
        return new Location(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }
}
