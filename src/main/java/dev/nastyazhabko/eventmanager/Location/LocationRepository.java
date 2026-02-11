package dev.nastyazhabko.eventmanager.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {

    @Transactional
    @Modifying
    @Query(
            """
                            UPDATE LocationEntity l
                                SET l.name = :name,
                                    l.address = :address,
                                    l.capacity = :capacity,
                                    l.description = :description
                                WHERE l.id = :id
                    """
    )
    void update(
            @Param("id") int id,
            @Param("name") String name,
            @Param("address") String address,
            @Param("capacity") Integer capacity,
            @Param("description") String description
    );
}
