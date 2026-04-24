package dev.nastyazhabko.eventnotificator.repository;

import dev.nastyazhabko.eventnotificator.entity.PayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PayloadRepository extends JpaRepository<PayloadEntity, Integer> {
    boolean existsByMessageId(UUID messageId);
}
