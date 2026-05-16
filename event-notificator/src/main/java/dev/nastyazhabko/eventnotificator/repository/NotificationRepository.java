package dev.nastyazhabko.eventnotificator.repository;

import dev.nastyazhabko.eventnotificator.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findAllByUserId(int userId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE notifications
            SET is_read=true
            WHERE user_id=:userId
            AND is_read=false
            RETURNING id
            """, nativeQuery = true
    )
    List<Integer> markAsReadByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM notifications
            WHERE created_at > :date
            AND is_read=true
            RETURNING payload_id
            """, nativeQuery = true)
    Set<Integer> deleteReadNotificationByDate(@Param("date") OffsetDateTime date);

    List<NotificationEntity> findByPayloadId(Integer payloadId);

    long countByUserIdAndIsReadFalse(Integer userId);
}
