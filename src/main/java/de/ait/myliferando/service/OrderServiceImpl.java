package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.Order;
import de.ait.myliferando.domain.enums.CourierStatus;
import de.ait.myliferando.domain.enums.OrderStatus;
import de.ait.myliferando.dto.mapping.OrderMapper;
import de.ait.myliferando.dto.order.OrderCreateRequest;
import de.ait.myliferando.dto.order.OrderResponse;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.repository.CourierRepository;
import de.ait.myliferando.repository.OrderRepository;
import de.ait.myliferando.service.interfaces.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final ClientRepository clientRepository;
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public OrderServiceImpl(ClientRepository clientRepository,
                            CourierRepository courierRepository,
                            OrderRepository orderRepository,
                            OrderMapper mapper) {
        this.clientRepository = clientRepository;
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderResponse create(OrderCreateRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(() ->
                new EntityNotFoundException(Client.class, request.getClientId()));
        Order entity = mapper.mapDtoToEntity(request);
        entity.setClient(client);
        orderRepository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse assignCourier(Long orderId, Long courierId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException(Order.class, orderId));
        Courier courier = courierRepository.findById(courierId).orElseThrow(()
                -> new EntityNotFoundException(Courier.class, courierId));
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidStatusException("Order " + orderId + " cannot be assigned in status: "
                    + order.getStatus());
        }
        if (courier.getStatus() != CourierStatus.AVAILABLE) {
            throw new InvalidStatusException("Courier " + courierId + " is not available, status: "
                    + courier.getStatus());
        }
        order.setCourier(courier);
        order.setStatus(OrderStatus.ASSIGNED);
        courier.setStatus(CourierStatus.BUSY);
        orderRepository.save(order);
        courierRepository.save(courier);
        return mapper.mapEntityToDto(order);
    }

    @Override
    @Transactional
    public OrderResponse pickUp(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException(Order.class, orderId));
        if (order.getStatus() != OrderStatus.ASSIGNED) {
            throw new InvalidStatusException("Order " + orderId + " cannot be picked up in status: "
                    + order.getStatus());
        }
        order.setStatus(OrderStatus.PICKED_UP);
        orderRepository.save(order);
        return mapper.mapEntityToDto(order);
    }

    @Override
    @Transactional
    public OrderResponse deliver(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException(Order.class, orderId));
        if (order.getStatus() != OrderStatus.PICKED_UP) {
            throw new InvalidStatusException("Order " + orderId + " cannot be delivered in status: "
                    + order.getStatus());
        }
        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        Courier courier = order.getCourier();
        courier.setStatus(CourierStatus.AVAILABLE);
        orderRepository.save(order);
        courierRepository.save(courier);
        return mapper.mapEntityToDto(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException(Order.class, orderId));
        if (order.getStatus() == OrderStatus.DELIVERED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidStatusException("Order: " + orderId + " cannot be cancelled in status: "
                    + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Courier courier = order.getCourier();
        if (courier != null) {
            courier.setStatus(CourierStatus.AVAILABLE);
            courierRepository.save(courier);
        }
        orderRepository.save(order);
        return mapper.mapEntityToDto(order);
    }
}
