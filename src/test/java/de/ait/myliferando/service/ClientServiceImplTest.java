package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.domain.User;
import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.mapping.ClientMapperImpl;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private ClientRepository repository;
    private ClientServiceImpl service;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClientRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        service = new ClientServiceImpl(repository, new ClientMapperImpl(), userRepository);
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
        String username = "client9";
        String name = "Ivan";
        String email = "max.musterman@gmail.com";
        String inn = "1234567890";

        ClientCreateRequest request = new ClientCreateRequest();
        request.setName(name);
        request.setEmail(email);
        request.setInn(inn);
        request.setUsername(username);

        when(repository.existsByEmail(email)).thenReturn(false);
        when(repository.existsByInn(inn)).thenReturn(false);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        ClientResponse result = service.save(request);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getInn()).isEqualTo(inn);

        verify(repository).save(any());
    }

    @Test
    void getByUsername_whenClientExists_returnResponse() {
        String username = "client2";
        String clientName = "Sushi Tokyo";
        Long clientId = 2L;
        Client client = new Client();
        client.setId(clientId);
        client.setName(clientName);

        when(repository.findByUserUsername(username)).thenReturn(Optional.of(client));
        ClientResponse result = service.getByUsername(username);
        assertThat(result.getName()).isEqualTo(clientName);
        assertThat(result.getId()).isEqualTo(clientId);
    }

    @Test
    void getByUsername_whenClientNotFound_throwsException() {
        String username = "client1";

        when(repository.findByUserUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getByUsername(username))
                .isInstanceOf(EntityNotFoundException.class);
    }
}