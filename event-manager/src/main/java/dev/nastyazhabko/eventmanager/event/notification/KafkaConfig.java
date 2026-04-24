package dev.nastyazhabko.eventmanager.event.notification;

import dev.nastyazhabko.eventcommon.kafka.EventChangeKafkaMessage;

import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.UUID;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaTemplate<UUID, EventChangeKafkaMessage> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {
        var props = kafkaProperties.buildProducerProperties();

        ProducerFactory<UUID, EventChangeKafkaMessage> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }
}
