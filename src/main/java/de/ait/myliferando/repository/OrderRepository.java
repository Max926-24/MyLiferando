package de.ait.myliferando.repository;

import de.ait.myliferando.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
