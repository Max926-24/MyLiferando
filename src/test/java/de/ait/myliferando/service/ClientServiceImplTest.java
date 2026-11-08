package de.ait.myliferando.service;

import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.mapping.ClientMapperImpl;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private ClientRepository repository;
    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClientRepository.class);
        service = new ClientServiceImpl(repository, new ClientMapperImpl());
    }

    @Test
    void save_whenEmailExists_throwsException() {
        String email = "max.musterman@gmail.com";

        ClientCreateRequest request = new ClientCreateRequest();
        request.setEmail(email);
        when(repository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(AlreadyExistsException.class);
        verify(repository, never()).save(any());

    }

    @Test
    void save_whenInnExists_throwsException() {
        String inn = "1234567890";
        String email = "max.musterman@gmail.com";
        ClientCreateRequest request = new ClientCreateRequest();
        request.setInn(inn);
        request.setEmail(email);

        when(repository.existsByInn(inn)).thenReturn(true);
        when(repository.existsByEmail(email)).thenReturn(false);

        assertThatThrownBy(() -> service.save(request))
                .isInstanceOf(AlreadyExistsException.class);
        verify(repository, never()).save(any());

    }

    @Test
    void save_whenEmailAndInnAreFree_returnsResponse() {
        String name = "Ivan";
        String email = "max.musterman@gmail.com";
        String inn = "1234567890";

        ClientCreateRequest request = new ClientCreateRequest();
        request.setName(name);
        request.setEmail(email);
        request.setInn(inn);

        when(repository.existsByEmail(email)).thenReturn(false);
        when(repository.existsByInn(inn)).thenReturn(false);
        ClientResponse result = service.save(request);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getInn()).isEqualTo(inn);

        verify(repository).save(any());


    }
}