package dev.nastyazhabko.eventmanager.event.notification;

import dev.nastyazhabko.eventcommon.kafka.ChangeFiled;
import dev.nastyazhabko.eventcommon.kafka.EventChangeKafkaMessage;
import dev.nastyazhabko.eventcommon.kafka.EventType;
import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventmanager.event.enums.EventStatus;
import dev.nastyazhabko.eventmanager.event.eventRegistration.entity.RegistrationEntity;
import dev.nastyazhabko.eventmanager.event.eventRegistration.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SendNotificationService {
    private final Logger log = LoggerFactory.getLogger(SendNotificationService.class);
    private final NotificationSender notificationSender;
    private final RegistrationRepository registrationRepository;
    private final String logMessage = "Sending notification in Kafka about event changed: {}";

    public SendNotificationService(NotificationSender notificationSender, RegistrationRepository registrationRepository) {
        this.notificationSender = notificationSender;
        this.registrationRepository = registrationRepository;
    }

    public void sendNotification(Event eventOld, Event eventNew, Integer userId) {
        List<Integer> subscribers = registrationRepository.findByEventId(eventOld.id())
                .stream()
                .map(RegistrationEntity::getUserId)
                .toList();

        EventChangeKafkaMessage message = new EventChangeKafkaMessage(
                UUID.randomUUID(),
                EventType.EVENT_UPDATED,
                eventOld.id(),
                eventNew.name(),
                eventNew.date(),
                eventOld.ownerId(),
                userId,
                subscribers,
                changedFields(eventOld, eventNew)
        );
        log.info(logMessage, message);
        notificationSender.sendNotification(message);
    }

    public void sendNotification(Event event, EventStatus newStatus, int userId) {
        List<Integer> subscribers = registrationRepository.findByEventId(event.id())
                .stream()
                .map(RegistrationEntity::getUserId)
                .toList();
        List<ChangeFiled> changedFields = List.of(
                new ChangeFiled("status",
                        event.status().toString(),
                        newStatus.toString()));

        EventChangeKafkaMessage message = new EventChangeKafkaMessage(
                UUID.randomUUID(),
                EventType.EVENT_CANCELLED,
                event.id(),
                event.name(),
                event.date(),
                event.ownerId(),
                userId,
                subscribers,
                changedFields
        );
        log.info(logMessage, message);
        notificationSender.sendNotification(message);
    }

    public void sendNotification(Event event, EventStatus newStatus) {
        List<Integer> subscribers = registrationRepository.findByEventId(event.id())
                .stream()
                .map(RegistrationEntity::getUserId)
                .toList();
        List<ChangeFiled> changedFields = List.of(
                new ChangeFiled("status",
                        event.status().toString(),
                        newStatus.toString()));

        EventChangeKafkaMessage message = new EventChangeKafkaMessage(
                UUID.randomUUID(),
                EventType.EVENT_STATUS_CHANGED,
                event.id(),
                event.name(),
                event.date(),
                event.ownerId(),
                null,
                subscribers,
                changedFields
        );
        log.info(logMessage, message);
        notificationSender.sendNotification(message);
    }

    private List<ChangeFiled> changedFields(Event eventOld, Event eventNew) {
        List<ChangeFiled> changedFields = new ArrayList<>();
        if (!eventOld.name().equals(eventNew.name())) {
            changedFields.add(new ChangeFiled(
                    "name",
                    eventOld.name(),
                    eventNew.name()
            ));
        }
        if (!eventOld.maxPlaces().equals(eventNew.maxPlaces())) {
            changedFields.add(new ChangeFiled(
                    "max_places",
                    eventOld.maxPlaces().toString(),
                    eventNew.maxPlaces().toString()
            ));
        }
        if (!eventOld.date().equals(eventNew.date())) {
            changedFields.add(new ChangeFiled(
                    "date",
                    eventOld.date().toString(),
                    eventNew.date().toString()
            ));
        }
        if (eventOld.cost().compareTo(eventNew.cost()) != 0) {
            changedFields.add(new ChangeFiled(
                    "cost",
                    eventOld.cost().toString(),
                    eventNew.cost().toString()
            ));
        }
        if (!eventOld.duration().equals(eventNew.duration())) {
            changedFields.add(new ChangeFiled(
                    "duration",
                    eventOld.duration().toString(),
                    eventNew.duration().toString()
            ));
        }
        if (!eventOld.locationId().equals(eventNew.locationId())) {
            changedFields.add(new ChangeFiled(
                    "locationId",
                    eventOld.locationId().toString(),
                    eventNew.locationId().toString()
            ));
        }

        return changedFields;

    }
}
