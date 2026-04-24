package dev.nastyazhabko.eventnotificator.entity;

import dev.nastyazhabko.eventcommon.kafka.EventType;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "event_id")
    private Integer eventId;
    @Column(name = "event_type")
    private EventType eventType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payload_id")
    private PayloadEntity payload;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public NotificationEntity() {
    }

    public NotificationEntity(Integer id,
                              Integer userId,
                              Integer eventId,
                              EventType eventType,
                              PayloadEntity payload,
                              Boolean isRead,
                              OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public PayloadEntity getPayload() {
        return payload;
    }

    public void setPayload(PayloadEntity payload) {
        this.payload = payload;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
