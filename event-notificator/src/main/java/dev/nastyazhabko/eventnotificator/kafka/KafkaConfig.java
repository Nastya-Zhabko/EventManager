package dev.nastyazhabko.eventnotificator.kafka;

import dev.nastyazhabko.eventcommon.kafka.EventChangeKafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public ConsumerFactory<UUID, EventChangeKafkaMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificator-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);

        var deserializer = new JacksonJsonDeserializer<>(EventChangeKafkaMessage.class, false);
        deserializer.addTrustedPackages("*");

        var factory = new DefaultKafkaConsumerFactory<UUID, EventChangeKafkaMessage>(props);
        factory.setValueDeserializer(deserializer);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, EventChangeKafkaMessage> containerFactory(
            ConsumerFactory<UUID, EventChangeKafkaMessage> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<UUID, EventChangeKafkaMessage>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
