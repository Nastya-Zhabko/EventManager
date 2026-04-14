package dev.nastyazhabko.eventmanager.location.repository;

import dev.nastyazhabko.eventmanager.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {}
