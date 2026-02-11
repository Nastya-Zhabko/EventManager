package dev.nastyazhabko.eventmanager.Location;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;
    private final LocationEntityConverter locationEntityConverter;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter locationEntityConverter) {
        this.locationRepository = locationRepository;
        this.locationEntityConverter = locationEntityConverter;
    }


    public Location createLocation(Location locationToCreate) {
        LocationEntity locationEntity = locationEntityConverter.toEntity(locationToCreate);
        logger.info("New location created: " + locationEntity);
        return locationEntityConverter.toDomain(
                locationRepository.save(locationEntity));
    }

    public List<Location> getAllLocations() {
        return locationRepository
                .findAll()
                .stream()
                .map(locationEntityConverter::toDomain)
                .toList();
    }

    public void deleteLocation(int id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Локация с id=" + id + " не найдена");
        }
        logger.info("Deleting location with id=" + id);
        locationRepository.deleteById(id);
    }

    public Location getLocationById(int id) {
        var location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Локация с id=" + id + " не найдена")
                );

        return locationEntityConverter.toDomain(location);
    }

    public Location updateLocation(int id, Location locationToUpdate) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Локация с id=" + id + " не найдена");
        }

        logger.info("Updating location: id = {}, locationToUpdate = {}", id, locationToUpdate);


        locationRepository.update(
                id,
                locationToUpdate.name(),
                locationToUpdate.address(),
                locationToUpdate.capacity(),
                locationToUpdate.description()
        );

        return getLocationById(id);
    }
}
