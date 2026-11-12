package de.ait.myliferando.service;

import de.ait.myliferando.domain.Courier;

import de.ait.myliferando.domain.User;
import de.ait.myliferando.domain.enums.CourierStatus;

import de.ait.myliferando.domain.enums.Transport;
import de.ait.myliferando.dto.courier.CourierCreateRequest;
import de.ait.myliferando.dto.courier.CourierResponse;
import de.ait.myliferando.dto.mapping.CourierMapperImpl;

import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.CourierRepository;
import de.ait.myliferando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;


import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class CourierServiceImplTest {

    private CourierRepository repository;
    private CourierServiceImpl service;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CourierRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        service = new CourierServiceImpl(repository, new CourierMapperImpl()
                , userRepository);
    }

    @Test
    void startShift_whenOffDuty_becomesAvailable() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.OFF_DUTY);
        User user = new User();
        user.setUsername("courier1");
        courier.setUser(user);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        CourierResponse result = service.startShift(1L, "courier1");
        assertThat(result.getStatus()).isEqualTo(CourierStatus.AVAILABLE);
        verify(repository).save(courier);

    }

    @Test
    void startShift_whenAnotherCourier_throwsException() {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.OFF_DUTY);
        User user = new User();
        user.setUsername("courier1");
        courier.setUser(user);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.startShift(1L, "courier2"))
                .isInstanceOf(AccessDeniedException.class);
        verify(repository, never()).save(any());
    }




    @Test
    void startShift_whenAlreadyAvailable_throwsException() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.AVAILABLE);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.startShift(1L, "courier1"))
                .isInstanceOf(InvalidStatusException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void startShift_whenCourierNotFound_throwsException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.startShift(1L, "courier1"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void endShift_whenAvailable_becomesOffDuty() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.AVAILABLE);
        User user = new User();
        user.setUsername("courier1");
        courier.setUser(user);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        CourierResponse result = service.endShift(1L, "courier1");
        assertThat(result.getStatus()).isEqualTo(CourierStatus.OFF_DUTY);
        verify(repository).save(courier);
    }
    @Test
    void endShift_whenAnotherCourier_throwsException(){
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.AVAILABLE);
        User user = new User();
        user.setUsername("courier1");
        courier.setUser(user);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.endShift(1L, "courier2"))
                .isInstanceOf(AccessDeniedException.class);
        verify(repository, never()).save(any());


    }

    @Test
    void endShift_whenAlreadyBusy_throwsException() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.BUSY);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.endShift(1L, "courier1"))
                .isInstanceOf(InvalidStatusException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void endShift_whenCourierNotFound_throwsException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.endShift(1L, "courier1"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void save_whenPhoneExists_throwsException() {

        CourierCreateRequest request = new CourierCreateRequest();
        request.setPhone("+491111");

        when(repository.existsByPhone("+491111")).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(AlreadyExistsException.class);
        verify(repository, never()).save(any());

    }

    @Test
    void save_whenPhoneIsFree_returnsResponse() {
        String phone = "+491111";
        CourierCreateRequest request = new CourierCreateRequest();
        request.setName("Ivan");
        request.setPhone(phone);
        request.setTransport(Transport.BIKE);
        request.setUsername("courier1");

        when(repository.existsByPhone(phone)).thenReturn(false);
        when(userRepository.findByUsername("courier1")).thenReturn(Optional
                .of(new User()));

        CourierResponse result = service.save(request);
        assertThat(result.getName()).isEqualTo("Ivan");
        assertThat(result.getPhone()).isEqualTo(phone);
        assertThat(result.getStatus()).isEqualTo(CourierStatus.OFF_DUTY);
        verify(repository).save(any());

    }


}

