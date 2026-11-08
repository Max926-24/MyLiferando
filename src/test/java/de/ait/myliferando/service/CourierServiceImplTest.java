package de.ait.myliferando.service;

import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.enums.CourierStatus;

import de.ait.myliferando.dto.courier.CourierResponse;
import de.ait.myliferando.dto.mapping.CourierMapperImpl;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


class CourierServiceImplTest {

    private CourierRepository repository;
    private CourierServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CourierRepository.class);
        service = new CourierServiceImpl(repository, new CourierMapperImpl());
    }

    @Test
    void startShift_whenOffDuty_becomesAvailable() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.OFF_DUTY);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        CourierResponse result = service.startShift(1L);
        assertThat(result.getStatus()).isEqualTo(CourierStatus.AVAILABLE);

    }

    @Test
    void startShift_whenAlreadyAvailable_throwsException() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.AVAILABLE);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.startShift(1L))
                .isInstanceOf(InvalidStatusException.class);
    }

    @Test
    void startShift_whenCourierNotFound_throwsException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.startShift(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void endShift_whenAvailable_becomesOffDuty() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.AVAILABLE);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        CourierResponse result = service.endShift(1L);
        assertThat(result.getStatus()).isEqualTo(CourierStatus.OFF_DUTY);

    }

    @Test
    void endShift_whenAlreadyBusy_throwsException() {

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setStatus(CourierStatus.BUSY);

        when(repository.findById(1L)).thenReturn(Optional.of(courier));

        assertThatThrownBy(() -> service.endShift(1L))
                .isInstanceOf(InvalidStatusException.class);
    }

    @Test
    void endShift_whenCourierNotFound_throwsException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.endShift(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }


}