package dev.nastyazhabko.eventmanager.event.eventRegistration.repository;

import dev.nastyazhabko.eventmanager.event.entity.EventEntity;
import dev.nastyazhabko.eventmanager.event.eventRegistration.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {
    @Query("""
            SELECT e.event FROM RegistrationEntity e
            WHERE e.userId = :id
            """)
    List<EventEntity> findByUserId(@Param("id") Integer id);

    Optional<RegistrationEntity> findByEventIdAndUserId(int eventId, int userId);

    List<RegistrationEntity> findByEventId(int eventId);
}
