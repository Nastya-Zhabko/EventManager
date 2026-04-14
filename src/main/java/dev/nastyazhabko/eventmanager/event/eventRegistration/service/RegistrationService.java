package dev.nastyazhabko.eventmanager.event.eventRegistration.service;

import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventmanager.event.entity.EventEntity;
import dev.nastyazhabko.eventmanager.event.enums.EventStatus;
import dev.nastyazhabko.eventmanager.event.eventRegistration.entity.RegistrationEntity;
import dev.nastyazhabko.eventmanager.event.eventRegistration.repository.RegistrationRepository;
import dev.nastyazhabko.eventmanager.event.repository.EventRepository;
import dev.nastyazhabko.eventmanager.event.converter.EventConverter;
import dev.nastyazhabko.eventmanager.user.dto.User;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public RegistrationService(RegistrationRepository registrationRepository, EventRepository eventRepository, EventConverter eventConverter) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
    }

    @Transactional
    public void createRegistration(int id, Integer userId) {
        var event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new EntityNotFoundException("Event with id= " + id + " not found");
        }
        if (event.get().getStatus() == EventStatus.STARTED ||event.get().getStatus() == EventStatus.FINISHED || event.get().getStatus() == EventStatus.CANCELLED) {
            throw new IllegalStateException("Event with id= " + id + " already finished");
        }
        if (event.get().getOccupiedPlaces() + 1 > event.get().getMaxPlaces()) {
            throw new IllegalStateException("Event with id= " + id + " have not enough places");
        }

        logger.info("Creating new registration for event with id= " + id);
        EventEntity eventEntity = event.get();
        RegistrationEntity registrationEntity = new RegistrationEntity(event.get(), userId);
        eventEntity.addRegistrations(registrationEntity);
        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() + 1);
        eventRepository.save(eventEntity);
        registrationRepository.save(registrationEntity);
    }

    @Transactional
    public void cancelRegistration(int id, Integer userId) {
        var event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new EntityNotFoundException("Event with id= " + id + " not found");
        }
        var registration = registrationRepository.findByEventIdAndUserId(id, userId);
        if (registration.isEmpty()) {
            throw new EntityNotFoundException("Registration on event with id= " + id + " not found");
        }
        if (event.get().getStatus() == EventStatus.FINISHED || event.get().getStatus() == EventStatus.CANCELLED) {
            throw new IllegalStateException("Event with id= " + id + " already finished");
        }
        if (event.get().getStatus() == EventStatus.STARTED) {
            throw new IllegalStateException("Event with id= " + id + " already started");
        }
        logger.info("Canceling registration for event with id= " + id);
        EventEntity eventEntity = event.get();
        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() - 1);
        eventEntity.removeRegistrations(registration.get());
        eventRepository.save(eventEntity);
        registrationRepository.delete(registration.get());
    }

    public List<Event> getCurrentUserEvents(User user) {
        return registrationRepository.findByUserId(user.id())
                .stream()
                .map(eventConverter::toDomain
                )
                .toList();
    }
}
