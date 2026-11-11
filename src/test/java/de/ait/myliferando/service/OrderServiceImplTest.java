package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.Order;
import de.ait.myliferando.domain.User;
import de.ait.myliferando.domain.enums.CourierStatus;
import de.ait.myliferando.domain.enums.OrderStatus;
import de.ait.myliferando.dto.mapping.OrderMapperImpl;
import de.ait.myliferando.dto.order.OrderCreateRequest;
import de.ait.myliferando.dto.order.OrderResponse;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.repository.CourierRepository;
import de.ait.myliferando.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private ClientRepository clientRepository;
    private CourierRepository courierRepository;
    private OrderRepository orderRepository;
    private OrderServiceImpl service;

    @BeforeEach
    void setUp() {
        clientRepository = Mockito.mock(ClientRepository.class);
        courierRepository = Mockito.mock(CourierRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        service = new OrderServiceImpl(clientRepository, courierRepository,
                orderRepository, new OrderMapperImpl());

    }

    @Test
    void assignCourier_whenOrderCreatedAndCourierAvailable_assigns() {

        Order order = new Order();
        order.setId(1L);

        Courier courier = new Courier();
        courier.setId(2L);
        courier.setStatus(CourierStatus.AVAILABLE);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(courierRepository.findById(2L)).thenReturn(Optional.of(courier));

        OrderResponse result = service.assignCourier(1L, 2L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ASSIGNED);
        assertThat(result.getCourierId()).isEqualTo(2L);
        assertThat(courier.getStatus()).isEqualTo(CourierStatus.BUSY);
        verify(orderRepository).save(order);
        verify(courierRepository).save(courier);

    }

    @Test
    void assignCourier_whenOrderNotCreated_throwsException() {

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CANCELLED);

        Courier courier = new Courier();
        courier.setId(2L);
        courier.setStatus(CourierStatus.AVAILABLE);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(courierRepository.findById(2L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.assignCourier(1L, 2L))
                .isInstanceOf(InvalidStatusException.class);

        verify(orderRepository, never()).save(any());
        verify(courierRepository, never()).save(any());

    }

    @Test
    void assignCourier_whenCourierNotAvailable_throwsException() {

        Order order = new Order();
        order.setId(1L);

        Courier courier = new Courier();
        courier.setId(2L);
        courier.setStatus(CourierStatus.BUSY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(courierRepository.findById(2L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.assignCourier(1L, 2L))
                .isInstanceOf(InvalidStatusException.class);

        verify(orderRepository, never()).save(any());
        verify(courierRepository, never()).save(any());

    }

    @Test
    void pickUp_whenAssigned_becomesPickedUp() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.ASSIGNED);
        User user = new User();
        user.setUsername("courier1");
        Courier courier = new Courier();
        courier.setUser(user);
        order.setCourier(courier);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderResponse result = service.pickUp(1L, "courier1");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PICKED_UP);
        verify(orderRepository).save(order);

    }

    @Test
    void pickUp_whenAnotherCourier_throwsException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.ASSIGNED);
        User user = new User();
        user.setUsername("courier1");
        Courier courier = new Courier();
        courier.setUser(user);
        order.setCourier(courier);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThatThrownBy(() -> service.pickUp(1L, "courier2"))
                .isInstanceOf(AccessDeniedException.class);
        verify(orderRepository, never()).save(any());

    }

    @Test
    void pickUp_whenNotAssigned_throwsException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThatThrownBy(() -> service.pickUp(1L, "courier1"))
                .isInstanceOf(InvalidStatusException.class);
        verify(orderRepository, never()).save(any());

    }

    @Test
    void deliver_whenPickedUp_becomesDelivered() {
        Courier courier = new Courier();
        courier.setId(2L);
        courier.setStatus(CourierStatus.BUSY);

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PICKED_UP);
        order.setCourier(courier);

        User user = new User();
        user.setUsername("courier1");
        courier.setUser(user);


        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderResponse result = service.deliver(1L, "courier1");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(result.getDeliveredAt()).isNotNull();
        assertThat(courier.getStatus()).isEqualTo(CourierStatus.AVAILABLE);
        verify(orderRepository).save(order);
        verify(courierRepository).save(courier);
    }

    @Test
    void deliver_whenAnotherCourier_throwsException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PICKED_UP);
        User user = new User();
        user.setUsername("courier1");
        Courier courier = new Courier();
        courier.setUser(user);
        order.setCourier(courier);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThatThrownBy(() -> service.deliver(1L, "courier2"))
                .isInstanceOf(AccessDeniedException.class);
        verify(orderRepository, never()).save(any());
        verify(courierRepository, never()).save(any());


    }

    @Test
    void deliver_whenNotPickedUp_throwsException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.ASSIGNED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThatThrownBy(() -> service.deliver(1L, "courier1"))
                .isInstanceOf(InvalidStatusException.class);

        verify(orderRepository, never()).save(any());
        verify(courierRepository, never()).save(any());

    }

    @Test
    void cancel_whenHasCourier_releasesCourier() {
        Courier courier = new Courier();
        courier.setId(2L);
        courier.setStatus(CourierStatus.BUSY);

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.ASSIGNED);
        order.setCourier(courier);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse result = service.cancel(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(courier.getStatus()).isEqualTo(CourierStatus.AVAILABLE);

        verify(orderRepository).save(order);
        verify(courierRepository).save(courier);

    }

    @Test
    void cancel_whenNoCourier_cancelsOnlyOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse result = service.cancel(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        verify(orderRepository).save(order);
        verify(courierRepository, never()).save(any());

    }

    @Test
    void cancel_whenDelivered_throwsException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.cancel(1L))
                .isInstanceOf(InvalidStatusException.class);
        verify(orderRepository, never()).save(any());
        verify(courierRepository, never()).save(any());
    }

    @Test
    void create_whenClientNotFound_throwsException() {
        OrderCreateRequest createRequest = new OrderCreateRequest();
        createRequest.setClientId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(createRequest))
                .isInstanceOf(EntityNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void create_whenClientExists_createsOrder() {
        String address = "MozartStr,1";

        Client client = new Client();
        client.setId(1L);
        client.setName("Pizza");

        OrderCreateRequest createRequest = new OrderCreateRequest();
        createRequest.setClientId(1L);
        createRequest.setDeliveryAddress(address);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        OrderResponse result = service.create(createRequest);

        assertThat(result.getClientId()).isEqualTo(1L);
        assertThat(result.getClientName()).isEqualTo("Pizza");
        assertThat(result.getDeliveryAddress()).isEqualTo(address);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);

        verify(orderRepository).save(any());


    }


}