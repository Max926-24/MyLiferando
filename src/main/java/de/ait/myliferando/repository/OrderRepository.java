package de.ait.myliferando.repository;

import de.ait.myliferando.domain.Order;
import de.ait.myliferando.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    List<Order> findByClientIdOrderByCreatedAtDesc(Long clientId);

    List<Order> findByCourierIdOrderByCreatedAtDesc(Long courierId);

    Optional<Order> findByCourierIdAndStatusIn(Long courierId, List<OrderStatus> statuses);
}
