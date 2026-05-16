package dev.nastyazhabko.eventnotificator.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nastyazhabko.eventcommon.kafka.EventChangeKafkaMessage;
import dev.nastyazhabko.eventnotificator.dto.Notification;
import dev.nastyazhabko.eventnotificator.dto.Payload;
import dev.nastyazhabko.eventnotificator.service.NotificationService;
import dev.nastyazhabko.eventnotificator.service.PayloadService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.UUID;

@Component
public class NotificationListener {
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final PayloadService payloadService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public NotificationListener(PayloadService payloadService, NotificationService notificationService, ObjectMapper objectMapper) {
        this.payloadService = payloadService;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${topic.name}", containerFactory = "containerFactory")
    public void listenNotification(
            ConsumerRecord<UUID, EventChangeKafkaMessage> record
    ) throws JsonProcessingException {
        if (payloadService.findPayloadByMessageId(record.key())) {
            log.info("Payload with message id {} already exists", record.key());
            return;
        }
        log.info("Received a new notification: {} ", record.value());

        var payload = new Payload(
                null,
                record.key(),
                objectMapper.writeValueAsString(new EventPayload(
                        record.value().event(),
                        record.value().changedById(),
                        record.value().changes()
                ))
        );

        Payload finalPayload = payloadService.savePayload(payload);

        List<Integer> subscribers = record.value().subscribers();

        subscribers.forEach(
                subscriber -> {
                    var notification = new Notification(
                            null,
                            subscriber,
                            record.value().eventId(),
                            record.value().eventType(),
                            finalPayload,
                            false,
                            record.value().occurredAt()
                    );
                    notificationService.saveNotification(notification);
                }
        );
    }
}
