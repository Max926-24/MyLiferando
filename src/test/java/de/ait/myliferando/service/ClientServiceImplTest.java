package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.User;
import de.ait.myliferando.domain.enums.ClientType;
import de.ait.myliferando.domain.enums.LegalType;
import de.ait.myliferando.domain.enums.Transport;
import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.client.ClientUpdateRequest;
import de.ait.myliferando.dto.courier.CourierUpdateRequest;
import de.ait.myliferando.dto.mapping.ClientMapperImpl;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.repository.OrderRepository;
import de.ait.myliferando.repository.UserRepository;
import jdk.dynalink.linker.support.CompositeTypeBasedGuardingDynamicLinker;
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
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClientRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        service = new ClientServiceImpl(repository, new ClientMapperImpl(),
                userRepository, orderRepository);
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

    @Test
    void getById_whenClientExists_returnsResponse() {
        Long clientId = 2L;
        String name = "Sushi Tokyo";
        Client client = new Client();
        client.setId(clientId);
        client.setName(name);

        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        ClientResponse result = service.getById(clientId);
        assertThat(result.getId()).isEqualTo(clientId);
        assertThat(result.getName()).isEqualTo(name);

    }

    @Test
    void getById_whenClientNotFound_throwsException() {
        Long clientId = 2L;

        when(repository.findById(clientId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(clientId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_whenEmailAndInnFree_updatesClient() {
        String newEmail = "tokyo.sushi@gmail.com";
        String newInn = "0987654321";

        Client client = new Client();
        client.setId(2L);
        client.setName("Pizza Pat");
        client.setEmail("max.musterman@gmail.com");
        client.setInn("1234567890");

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Tokyo Sushi");
        updateRequest.setAddress("Lenina,10");
        updateRequest.setEmail(newEmail);
        updateRequest.setPhone("+4915170186917");
        updateRequest.setClientType(ClientType.RESTAURANT);
        updateRequest.setLegalType(LegalType.LEGAL_ENTITY);
        updateRequest.setInn(newInn);

        when(repository.findById(2L)).thenReturn(Optional.of(client));
        when(repository.findByEmail(newEmail))
                .thenReturn(Optional.empty());
        when(repository.findByInn(newInn)).thenReturn(Optional.empty());

        ClientResponse result = service.update(2L, updateRequest);
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Tokyo Sushi");
        assertThat(result.getAddress()).isEqualTo("Lenina,10");
        assertThat(result.getEmail()).isEqualTo(newEmail);
        assertThat(result.getPhone()).isEqualTo("+4915170186917");
        assertThat(result.getInn()).isEqualTo(newInn);

        verify(repository).save(client);
    }

    @Test
    void update_whenEmailAndInnAreOwn_updatesClient() {
        String ownEmail = "max.musterman@gmail.com";
        String ownInn = "1234567890";

        Client client = new Client();
        client.setId(2L);
        client.setEmail(ownEmail);
        client.setInn(ownInn);
        client.setName("Pizza Pat");

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Tokyo Sushi");
        updateRequest.setAddress("Lenina,10");
        updateRequest.setEmail(ownEmail);
        updateRequest.setPhone("+4915170186917");
        updateRequest.setClientType(ClientType.RESTAURANT);
        updateRequest.setLegalType(LegalType.LEGAL_ENTITY);
        updateRequest.setInn(ownInn);

        when(repository.findById(2L)).thenReturn(Optional.of(client));
        when(repository.findByEmail(ownEmail)).thenReturn(Optional.of(client));
        when(repository.findByInn(ownInn)).thenReturn(Optional.of(client));

        ClientResponse result = service.update(2L, updateRequest);

        assertThat(result.getEmail()).isEqualTo(ownEmail);
        assertThat(result.getInn()).isEqualTo(ownInn);
        assertThat(result.getName()).isEqualTo("Tokyo Sushi");

        verify(repository).save(client);
    }

    @Test
    void update_whenEmailBelongsToAnotherClient_throwsException() {

        String otherEmail = "max.musterman@gmail.com";
        Client client = new Client();
        client.setId(2L);
        client.setEmail("pizzapat@gmail.com");

        Client otherClient = new Client();
        otherClient.setId(1L);
        otherClient.setEmail(otherEmail);

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("PizzaMania");
        updateRequest.setPhone("+491514395866");
        updateRequest.setEmail(otherEmail);
        updateRequest.setInn("0978654323");
        updateRequest.setLegalType(LegalType.LEGAL_ENTITY);
        updateRequest.setClientType(ClientType.RESTAURANT);

        when(repository.findById(2L)).thenReturn(Optional.of(client));
        when(repository.findByEmail(otherEmail)).thenReturn(Optional.of(otherClient));
        assertThatThrownBy(() -> service.update(2L, updateRequest))
                .isInstanceOf(AlreadyExistsException.class);
        verify(repository, never()).save(any());
        verify(repository, never()).findByInn(any());
    }

    @Test
    void update_whenInnBelongsToAnotherClient_throwsException() {

        String newEmail = "max.musterman@gmail.com";
        String otherInn = "0987653532";

        Client client = new Client();
        client.setId(2L);
        client.setInn("2134566780");
        client.setEmail("maxfactor@gmail.com");

        Client otherClient = new Client();
        otherClient.setId(1L);
        otherClient.setInn(otherInn);

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("PizzaMania");
        updateRequest.setPhone("+491514395866");
        updateRequest.setEmail(newEmail);
        updateRequest.setInn(otherInn);
        updateRequest.setLegalType(LegalType.LEGAL_ENTITY);
        updateRequest.setClientType(ClientType.RESTAURANT);

        when(repository.findById(2L)).thenReturn(Optional.of(client));
        when(repository.findByEmail(newEmail)).thenReturn(Optional.empty());
        when(repository.findByInn(otherInn)).thenReturn(Optional.of(otherClient));

        assertThatThrownBy(() -> service.update(2L, updateRequest))
                .isInstanceOf(AlreadyExistsException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void update_whenClientNotFound_throwsException() {

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Mark");
        updateRequest.setEmail("max.musterman@gmail.com");
        updateRequest.setPhone("+4915170109877");
        updateRequest.setClientType(ClientType.RESTAURANT);
        updateRequest.setLegalType(LegalType.LEGAL_ENTITY);

        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(2L, updateRequest))
                .isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).save(any());
        verify(repository, never()).findByEmail(any());
        verify(repository, never()).findByInn(any());
    }

    @Test
    void update_whenInnIsNull_doesNotCheckInn() {

        String newEmail = "max.musterman@gmail.com";

        Client client = new Client();
        client.setId(2L);
        client.setInn("1234567890");

        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Mark");
        updateRequest.setEmail(newEmail);
        updateRequest.setPhone("+4915170109877");
        updateRequest.setClientType(ClientType.RESTAURANT);
        updateRequest.setLegalType(LegalType.LEGAL_ENTITY);

        when(repository.findById(2L)).thenReturn(Optional.of(client));
        when(repository.findByEmail(newEmail)).thenReturn(Optional.empty());

        ClientResponse result = service.update(2L, updateRequest);
        assertThat(result.getInn()).isNull();

        verify(repository).save(client);
        verify(repository, never()).findByInn(any());
    }

    @Test
    void delete_whenNoOrders_deletesClient() {
        Long clientId = 2L;
        Client client = new Client();
        client.setId(clientId);

        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.existsByClientId(clientId)).thenReturn(false);
        service.delete(clientId);
        verify(repository).delete(client);
    }

    @Test
    void delete_whenClientHasOrders_throwsException() {
        Long clientId = 2L;
        Client client = new Client();
        client.setId(clientId);

        when(repository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.existsByClientId(clientId)).thenReturn(true);
        assertThatThrownBy(() -> service.delete(clientId))
                .isInstanceOf(InvalidStatusException.class);
        verify(repository, never()).delete(any());
    }

    @Test
    void delete_whenClientNotFound_throwsException() {

        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(2L))
                .isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).delete(any());
        verify(orderRepository, never()).existsByClientId(any());
    }

}