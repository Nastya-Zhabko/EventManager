package dev.nastyazhabko.eventnotificator.service;

import dev.nastyazhabko.eventnotificator.converter.PayloadConverter;
import dev.nastyazhabko.eventnotificator.dto.Payload;
import dev.nastyazhabko.eventnotificator.entity.PayloadEntity;
import dev.nastyazhabko.eventnotificator.repository.PayloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PayloadService {
    private static final Logger log = LoggerFactory.getLogger(PayloadService.class);
    private final PayloadRepository payloadRepository;
    private final PayloadConverter payloadConverter;

    public PayloadService(PayloadRepository payloadRepository, PayloadConverter payloadConverter) {
        this.payloadRepository = payloadRepository;
        this.payloadConverter = payloadConverter;
    }

    public Payload savePayload(Payload payload) {
        PayloadEntity payloadEntity = new PayloadEntity(
                payload.id(),
                payload.messageId(),
                payload.payload());
        payloadEntity = payloadRepository.save(payloadEntity);

        log.info("Payload saved: {}", payloadEntity);

        return payloadConverter.toDomain(payloadEntity);
    }

    public boolean findPayloadByMessageId(UUID messageId) {
        log.info("Check existing payload with message ID: {}", messageId);
        return payloadRepository.existsByMessageId(messageId);
    }

    public void deletePayloadById(Integer id) {
        log.info("Delete payload by id: {}", id);
        payloadRepository.deleteById(id);
    }
}
