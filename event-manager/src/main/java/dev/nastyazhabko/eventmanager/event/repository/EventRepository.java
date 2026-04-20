package dev.nastyazhabko.eventmanager.event.repository;

import dev.nastyazhabko.eventmanager.event.enums.EventStatus;
import dev.nastyazhabko.eventmanager.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    List<EventEntity> findByOwnerId(int id);

    @Query("""
                SELECT e
                FROM EventEntity e
                WHERE (e.name LIKE COALESCE(:name, e.name))
                           AND (e.maxPlaces >= COALESCE(:placesMin, e.maxPlaces))
                           AND (e.maxPlaces <= COALESCE(:placesMax, e.maxPlaces))
                           AND (e.date >= COALESCE(:dateStartAfter, e.date))
                           AND (e.date <= COALESCE(:dateStartBefore, e.date))
                           AND (e.cost >= COALESCE(:costMin, e.cost))
                           AND (e.cost <= COALESCE(:costMax, e.cost))
                           AND (e.duration >= COALESCE(:durationMin, e.duration))
                           AND (e.duration <= COALESCE(:durationMax, e.duration))
                           AND (e.location.id = COALESCE(:locationId, e.location.id))
                           AND (e.status = COALESCE(:eventStatus, e.status))
           """
    )
    List<EventEntity> searchEventsByFilter(@Param("name") String name,
                                     @Param("placesMin") Integer placesMin,
                                     @Param("placesMax") Integer placesMax,
                                     @Param("dateStartBefore") OffsetDateTime dateStartBefore,
                                     @Param("dateStartAfter") OffsetDateTime dateStartAfter,
                                     @Param("costMin") BigDecimal costMin,
                                     @Param("costMax") BigDecimal costMax,
                                     @Param("durationMin") Integer durationMin,
                                     @Param("durationMax") Integer durationMax,
                                     @Param("locationId") Integer locationId,
                                     @Param("eventStatus") EventStatus eventStatus);

    List<EventEntity> findEventsByStatus(EventStatus status);

    boolean existsByLocationId(int id);
}
