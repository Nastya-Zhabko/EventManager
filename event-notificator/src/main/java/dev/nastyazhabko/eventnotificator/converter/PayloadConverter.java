package dev.nastyazhabko.eventnotificator.converter;

import dev.nastyazhabko.eventnotificator.dto.Payload;
import dev.nastyazhabko.eventnotificator.entity.PayloadEntity;
import org.springframework.stereotype.Component;

@Component
public class PayloadConverter {
    public Payload toDomain(PayloadEntity payload) {
        return new Payload(
                payload.getId(),
                payload.getMessageId(),
                payload.getPayload()
        );
    }

    public PayloadEntity toEntity(Payload payload) {
        return new PayloadEntity(
                payload.id(),
                payload.messageId(),
                payload.payload()
        );
    }
}
