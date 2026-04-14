package dev.nastyazhabko.eventmanager.event.entity;

import dev.nastyazhabko.eventmanager.event.enums.EventStatus;
import dev.nastyazhabko.eventmanager.event.eventRegistration.entity.RegistrationEntity;
import dev.nastyazhabko.eventmanager.location.entity.LocationEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "owner_id")
    private Integer ownerId;
    @Column(name = "max_places")
    private Integer maxPlaces;
    @Column(name = "occupied_places")
    private Integer occupiedPlaces;
    @Column(name = "date")
    private OffsetDateTime date;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "duration")
    private Integer duration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;
    @Column(name = "status")
    private EventStatus status;
    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RegistrationEntity> registrations;

    public EventEntity() {
    }

    public EventEntity(Integer id,
                       String name,
                       Integer ownerId,
                       Integer maxPlaces,
                       Integer occupiedPlaces,
                       OffsetDateTime date,
                       BigDecimal cost,
                       Integer duration,
                       LocationEntity location,
                       EventStatus status) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.location = location;
        this.status = status;
        this.registrations = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<RegistrationEntity> getRegistrations() {
        return registrations;
    }

    public void addRegistrations(RegistrationEntity registration) {
        registrations.add(registration);
    }

    public void removeRegistrations(RegistrationEntity registration) {
        registrations.remove(registration);
    }
}
