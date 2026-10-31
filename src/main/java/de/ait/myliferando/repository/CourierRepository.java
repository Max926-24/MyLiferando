package de.ait.myliferando.repository;

import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.enums.CourierStatus;

import de.ait.myliferando.domain.enums.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long> {

    boolean existsByPhone(String phone);

    Optional<Courier> findByPhone(String phone);

    List<Courier> findByStatus(CourierStatus status);

    List<Courier> findByStatusAndTransport(CourierStatus status, Transport transport);

    List<Courier> findByNameContainingIgnoreCase(String name);
}
