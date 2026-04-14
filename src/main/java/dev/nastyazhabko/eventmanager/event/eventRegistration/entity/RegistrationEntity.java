package dev.nastyazhabko.eventmanager.event.eventRegistration.entity;

import dev.nastyazhabko.eventmanager.event.entity.EventEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "registrations")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(name = "user_id")
    private Integer userId;

    public RegistrationEntity() {
    }

    public RegistrationEntity(EventEntity event, Integer userId) {
        this.event = event;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
