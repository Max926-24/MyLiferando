package de.ait.myliferando.service.interfaces;

import de.ait.myliferando.dto.order.OrderCreateRequest;
import de.ait.myliferando.dto.order.OrderResponse;


import java.util.List;

public interface OrderService {

    OrderResponse create(OrderCreateRequest request);

    List<OrderResponse> findAll();

    OrderResponse assignCourier(Long orderId, Long courierId);

    OrderResponse pickUp(Long orderId, String username);

    OrderResponse deliver(Long orderId);

    OrderResponse cancel(Long orderId);
}
