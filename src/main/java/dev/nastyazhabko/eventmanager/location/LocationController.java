package dev.nastyazhabko.eventmanager.location;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationDtoConverter locationDtoConverter;

    public LocationController(LocationService locationService, LocationDtoConverter locationDtoConverter) {
        this.locationService = locationService;
        this.locationDtoConverter = locationDtoConverter;
    }

    @GetMapping
    public List<LocationDto> getAllLocations() {
        logger.info("Get request for all locations");

        return locationService.getAllLocations()
                .stream()
                .map(locationDtoConverter::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public LocationDto getLocationById(@PathVariable int id) {
        logger.info("Get request for location with id {}", id);

        return locationDtoConverter
                .toDto(locationService.getLocationById(id));
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody @Valid LocationDto locationToCreate) {
        logger.info("Get request for create location: {}", locationToCreate);

        Location createdLocation = locationService.createLocation(locationDtoConverter.toDomain(locationToCreate));

        return ResponseEntity.status(HttpStatus.CREATED).body(locationDtoConverter.toDto(createdLocation));
    }

    @PutMapping("/{id}")
    public LocationDto updateLocation(
            @RequestBody @Valid LocationDto locationToUpdate,
            @PathVariable int id) {
        logger.info("Get request for updateLocation: id = {}, locationToUpdate = {}", id, locationToUpdate);

        var location = locationService.updateLocation(
                id,
                locationDtoConverter.toDomain(locationToUpdate));

        return locationDtoConverter.toDto(location);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable int id) {
        logger.info("Get request for delete location: {}", id);

        locationService.deleteLocation(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
