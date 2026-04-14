package dev.nastyazhabko.eventmanager.event.converter;

import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventmanager.event.dto.EventDto;
import dev.nastyazhabko.eventmanager.event.dto.EventToCreateAndUpdateDto;
import dev.nastyazhabko.eventmanager.event.entity.EventEntity;
import dev.nastyazhabko.eventmanager.location.entity.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    public EventEntity toEntity(Event event, LocationEntity location) {
        return new EventEntity(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                location,
                event.status()
        );
    }

    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public Event toDomain(EventEntity event) {
        return new Event(
                event.getId(),
                event.getName(),
                event.getOwnerId(),
                event.getMaxPlaces(),
                event.getOccupiedPlaces(),
                event.getDate(),
                event.getCost(),
                event.getDuration(),
                event.getLocation().getId(),
                event.getStatus()
        );
    }

    public Event toDomain(EventToCreateAndUpdateDto eventDto) {
        return new Event(
                null,
                eventDto.name(),
                null,
                eventDto.maxPlaces(),
                null,
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId(),
                null);
    }
}
