package dev.nastyazhabko.eventmanager.location.service;

import dev.nastyazhabko.eventmanager.event.repository.EventRepository;
import dev.nastyazhabko.eventmanager.location.converter.LocationEntityConverter;
import dev.nastyazhabko.eventmanager.location.dto.Location;
import dev.nastyazhabko.eventmanager.location.entity.LocationEntity;
import dev.nastyazhabko.eventmanager.location.exception.LocationHaveEventsException;
import dev.nastyazhabko.eventmanager.location.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;
    private final LocationEntityConverter locationEntityConverter;
    private final EventRepository eventRepository;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter locationEntityConverter, EventRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.locationEntityConverter = locationEntityConverter;
        this.eventRepository = eventRepository;
    }


    @CacheEvict(
            value = "locations",
            key = "'all'"
    )
    public Location createLocation(Location locationToCreate) {
        LocationEntity locationEntity = locationEntityConverter.toEntity(locationToCreate);
        logger.info("New location created: " + locationEntity);
        return locationEntityConverter.toDomain(
                locationRepository.save(locationEntity));
    }

    @Cacheable(
            value = "locations",
            key = "'all'",
            cacheManager = "listLocationCacheManager"
    )
    public List<Location> getAllLocations() {
        return locationRepository
                .findAll()
                .stream()
                .map(locationEntityConverter::toDomain)
                .toList();
    }

    @Caching(evict = {
            @CacheEvict(
                    value = "location",
                    key = "'id:' + #id"
            ),
            @CacheEvict(
                    value = "locations",
                    key = "'all'"
            )
    })
    public void deleteLocation(int id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Локация с id=" + id + " не найдена");
        }
        if (eventRepository.existsByLocationId(id)) {
            throw new LocationHaveEventsException("На локации с id=" + id + " зарегистрированы мероприятия");
        }
        logger.info("Deleting location with id=" + id);
        locationRepository.deleteById(id);
    }

    @Cacheable(
            value = "location",
            key = "'id:' + #id",
            cacheManager = "locationCacheManager")
    public Location getLocationById(int id) {
        var location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Локация с id=" + id + " не найдена")
                );

        return locationEntityConverter.toDomain(location);
    }

    @Caching(evict = {
            @CacheEvict(
                    value = "location",
                    key = "'id:' + #id"
            ),
            @CacheEvict(
                    value = "locations",
                    key = "'all'"
            )
    })
    public Location updateLocation(int id, Location locationToUpdate) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Локация с id=" + id + " не найдена");
        }
        var oldLocationCapacity = locationRepository.findById(id).get().getCapacity();
        if (oldLocationCapacity > locationToUpdate.capacity()) {
            throw new IllegalStateException("Новая вместимость локации должна быть больше чем текущая: "
                    + oldLocationCapacity);
        }

        logger.info("Updating location: id = {}, locationToUpdate = {}", id, locationToUpdate);

        Location locationToUpdateWithId = new Location(
                id,
                locationToUpdate.name(),
                locationToUpdate.address(),
                locationToUpdate.capacity(),
                locationToUpdate.description()
        );

        locationRepository.save(locationEntityConverter.toEntity(locationToUpdateWithId));

        return getLocationById(id);
    }
}
