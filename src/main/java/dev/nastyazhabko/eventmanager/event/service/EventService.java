package dev.nastyazhabko.eventmanager.event.service;

import dev.nastyazhabko.eventmanager.event.converter.EventConverter;
import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventmanager.event.dto.EventSearchFilter;
import dev.nastyazhabko.eventmanager.event.entity.EventEntity;
import dev.nastyazhabko.eventmanager.event.enums.EventStatus;
import dev.nastyazhabko.eventmanager.event.repository.EventRepository;
import dev.nastyazhabko.eventmanager.location.repository.LocationRepository;
import dev.nastyazhabko.eventmanager.security.jwt.AuthenticationService;
import dev.nastyazhabko.eventmanager.user.dto.User;
import dev.nastyazhabko.eventmanager.event.exception.UserNotAdminAndNotOwnerOfEventException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventConverter eventConverter;
    private final AuthenticationService authenticationService;
    private final EventPermissionService eventPermissionService;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository, EventConverter eventConverter, AuthenticationService authenticationService, EventPermissionService eventPermissionService) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventConverter = eventConverter;
        this.authenticationService = authenticationService;
        this.eventPermissionService = eventPermissionService;
    }

    public Event createEvent(Event event, User user) {
        var location = locationRepository.findById(event.locationId());
        if (location.isEmpty()) {
            throw new EntityNotFoundException("Location with id= " + event.locationId() + " not found");
        }
        if (event.maxPlaces() > location.get().getCapacity()) {
            throw new IllegalArgumentException("Capacity of location is greater than capacity of event");
        }
        EventEntity eventEntity = eventConverter.toEntity(event, location.get());
        eventEntity.setOwnerId(user.id());
        eventEntity.setOccupiedPlaces(0);
        eventEntity.setStatus(EventStatus.WAIT_START);


        logger.info("Creating new event: {}", eventEntity);
        return eventConverter.toDomain(eventRepository.save(eventEntity));
    }

    public void softDeleteEvent(Integer id) {
        var event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new EntityNotFoundException("Event with id= " + id + " not found");
        }
        if (!eventPermissionService.canModifyEvent(getCurrentUser(), eventConverter.toDomain(event.get()))) {
            throw new UserNotAdminAndNotOwnerOfEventException("You do not have permission to delete this event");
        }
        logger.info("Canceling event with id=" + id);
        event.get().setStatus(EventStatus.CANCELLED);
        eventRepository.save(event.get());
    }

    public Event getEventById(Integer id) {
        var event = eventRepository.findById(id)
                .orElseThrow(
                        () ->
                                new EntityNotFoundException("Event with id= " + id + " not found")
                );
        return eventConverter.toDomain(event);
    }

    public Event updateEvent(Integer id, Event eventToUpdate) {
        var event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new EntityNotFoundException("Event with id= " + id + " not found");
        }
        if (!eventPermissionService.canModifyEvent(getCurrentUser(), eventConverter.toDomain(event.get()))) {
            throw new UserNotAdminAndNotOwnerOfEventException("You do not have permission to update this event");
        }
        var location = locationRepository.findById(eventToUpdate.locationId());
        if (location.isEmpty()) {
            throw new EntityNotFoundException("Location with id= " + eventToUpdate.locationId() + " not found");
        }
        if (eventToUpdate.maxPlaces() > location.get().getCapacity()) {
            throw new IllegalArgumentException("Capacity of location is greater than capacity of event");
        }

        logger.info("Updating event: id = {}, eventToUpdate = {}", id, eventToUpdate);

        EventEntity eventEntity = event.get();
        eventEntity.setName(eventToUpdate.name());
        eventEntity.setMaxPlaces(eventToUpdate.maxPlaces());
        eventEntity.setDate(eventToUpdate.date());
        eventEntity.setCost(eventToUpdate.cost());
        eventEntity.setDuration(eventToUpdate.duration());
        eventEntity.setLocation(locationRepository.getReferenceById(eventToUpdate.locationId()));

        return eventConverter.toDomain(eventRepository.save(eventEntity));
    }

    public List<Event> searchEventsByFilter(EventSearchFilter eventSearchFilter) {
        return eventRepository.searchEventsByFilter(
                        eventSearchFilter.name(),
                        eventSearchFilter.placesMin(),
                        eventSearchFilter.placesMax(),
                        eventSearchFilter.dateStartBefore(),
                        eventSearchFilter.dateStartAfter(),
                        eventSearchFilter.costMin(),
                        eventSearchFilter.costMax(),
                        eventSearchFilter.durationMin(),
                        eventSearchFilter.durationMax(),
                        eventSearchFilter.locationId(),
                        eventSearchFilter.eventStatus()
                ).stream()
                .map(eventConverter::toDomain)
                .toList();
    }

    public List<Event> getCurrentUserEvents(User user) {
        return eventRepository.findByOwnerId(user.id())
                .stream()
                .map(eventConverter::toDomain)
                .toList();
    }

    private User getCurrentUser() {
        return authenticationService.getCurrentAuthenticatedUserOrThrow();
    }

    @Scheduled(cron = "${event.status.cron}")
    public void updateStatuses() {
        var started = eventRepository.findEventsByStatus(EventStatus.WAIT_START);
        started.forEach(event -> {
            if (OffsetDateTime.now()
                    .isAfter(event.getDate())) {
                event.setStatus(EventStatus.STARTED);
                eventRepository.save(event);
            }
        });

        var finished = eventRepository.findEventsByStatus(EventStatus.STARTED);
        finished.forEach(event -> {
            if (OffsetDateTime.now()
                    .isAfter(event.getDate()
                            .plusDays(event.getDuration()))) {
                event.setStatus(EventStatus.FINISHED);
                eventRepository.save(event);
            }
        });
    }
}
