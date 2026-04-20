package dev.nastyazhabko.eventmanager.event.notification;

import dev.nastyazhabko.eventcommon.kafka.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationSender {
    private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);
    private final KafkaTemplate<UUID, EventChangeKafkaMessage> kafkaTemplate;
    @Value("${topic.name}")
    private String topic;

    public NotificationSender(KafkaTemplate<UUID, EventChangeKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(EventChangeKafkaMessage message) {
        log.info("Sending notification about event changing: {}", message);
        var result = kafkaTemplate.send(
                topic,
                message.messageId(),
                message
        );

        result.thenAccept(sendResult -> {
            log.info("Notification sent successfully");
        });
    }
}
