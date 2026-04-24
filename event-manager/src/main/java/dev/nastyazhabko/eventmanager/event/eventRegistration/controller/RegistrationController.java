package dev.nastyazhabko.eventmanager.event.eventRegistration.controller;

import dev.nastyazhabko.eventmanager.event.converter.EventConverter;
import dev.nastyazhabko.eventmanager.event.dto.EventDto;
import dev.nastyazhabko.eventmanager.event.eventRegistration.service.RegistrationService;
import dev.nastyazhabko.eventmanager.security.jwt.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final EventConverter eventConverter;

    public RegistrationController(RegistrationService registrationService, AuthenticationService authenticationService, EventConverter eventConverter) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.eventConverter = eventConverter;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> createRegistration(@PathVariable int id) {
        logger.info("Get request for creating registration on event with id {}", id);

        var currentUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        registrationService.createRegistration(id, currentUser.id());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable int id) {
        logger.info("Get request for cancelling registration on event with id {}", id);

        var currentUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        registrationService.cancelRegistration(id, currentUser.id());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    public List<EventDto> getCurrentUserEvents() {
        logger.info("Get request for for current user events");

        var currentUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        return registrationService.getCurrentUserEvents(currentUser)
                .stream()
                .map(eventConverter::toDto)
                .toList();
    }
}
