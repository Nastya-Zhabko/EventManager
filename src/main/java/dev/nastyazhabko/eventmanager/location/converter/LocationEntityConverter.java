package dev.nastyazhabko.eventmanager.location.converter;

import dev.nastyazhabko.eventmanager.location.entity.LocationEntity;
import dev.nastyazhabko.eventmanager.location.dto.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationEntityConverter {
    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(LocationEntity location) {
        return new Location(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription()
        );
    }
}
