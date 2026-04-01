package dev.nastyazhabko.eventmanager.event.controller;

import dev.nastyazhabko.eventmanager.event.converter.EventConverter;
import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventmanager.event.dto.EventDto;
import dev.nastyazhabko.eventmanager.event.dto.EventSearchFilter;
import dev.nastyazhabko.eventmanager.event.dto.EventToCreateAndUpdateDto;
import dev.nastyazhabko.eventmanager.event.service.EventService;
import dev.nastyazhabko.eventmanager.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;
    private final AuthenticationService authenticationService;
    private final EventConverter eventConverter;

    public EventController(EventService eventService, AuthenticationService authenticationService, EventConverter eventConverter) {
        this.eventService = eventService;
        this.authenticationService = authenticationService;
        this.eventConverter = eventConverter;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody @Valid EventToCreateAndUpdateDto eventDto) {
        log.info("Get request for create event: {}", eventDto);
        var currenUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        Event createdEvent = eventService.createEvent(eventConverter.toDomain(eventDto), currenUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventConverter.toDto(createdEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        log.info("Get request for delete event: {}", id);

        eventService.softDeleteEvent(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable Integer id) {
        log.info("Get request for get event: {}", id);

        return eventConverter.toDto(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public EventDto updateEvent(@PathVariable Integer id,
                                @RequestBody @Valid EventToCreateAndUpdateDto eventToUpdate) {
        log.info("Get request for update event: id = {}, eventToUpdate = {}", id, eventToUpdate);
        var event = eventService.updateEvent(
                id,
                eventConverter.toDomain(eventToUpdate)
        );

        return eventConverter.toDto(event);
    }

    @PostMapping("/search")
    public List<EventDto> getEventsByFilter(@Valid @RequestBody EventSearchFilter eventSearchFilter) {
        log.info("Get request for search events by filter: {}", eventSearchFilter);

        return eventService.searchEventsByFilter(eventSearchFilter).
                stream()
                .map(eventConverter::toDto)
                .toList();
    }

    @GetMapping("/my")
    public List<EventDto> getCurrentUserEvents() {
        log.info("Get request for current user events");

        var currenUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        return eventService.getCurrentUserEvents(currenUser)
                .stream()
                .map(eventConverter::toDto)
                .toList();
    }
}
