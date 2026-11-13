package de.ait.myliferando.controller;


import de.ait.myliferando.dto.order.OrderCreateRequest;
import de.ait.myliferando.dto.order.OrderResponse;
import de.ait.myliferando.service.interfaces.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderCreateRequest request, Principal principal) {
        return service.create(request, principal.getName());
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return service.findAll();
    }

    @PatchMapping("/{orderId}/assign/{courierId}")
    public OrderResponse assignCourier(@PathVariable Long orderId,
                                       @PathVariable Long courierId) {
        return service.assignCourier(orderId, courierId);
    }

    @PatchMapping("/{id}/pick-up")
    public OrderResponse pickUp(@PathVariable Long id, Principal principal) {
        return service.pickUp(id, principal.getName());
    }

    @PatchMapping("/{id}/deliver")
    public OrderResponse deliver(@PathVariable Long id, Principal principal) {
        return service.deliver(id, principal.getName());
    }

    @PatchMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id, Principal principal) {
        return service.cancel(id, principal.getName());
    }

}
