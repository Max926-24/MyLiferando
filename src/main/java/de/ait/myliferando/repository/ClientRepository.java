package de.ait.myliferando.repository;

import de.ait.myliferando.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByInn(String inn);

    boolean existsByEmail(String email);

    Optional<Client> findByInn(String inn);

    List<Client> findByNameContainingIgnoreCase(String name);

}
