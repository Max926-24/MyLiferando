package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.domain.User;
import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.client.ClientUpdateRequest;
import de.ait.myliferando.dto.mapping.ClientMapper;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.repository.OrderRepository;
import de.ait.myliferando.repository.UserRepository;
import de.ait.myliferando.service.interfaces.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ClientServiceImpl(ClientRepository repository,
                             ClientMapper mapper,
                             UserRepository userRepository,
                             OrderRepository orderRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ClientResponse save(ClientCreateRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Client with email " + request.getEmail() +
                    " already exists");
        }
        if (request.getInn() != null && repository.existsByInn(request.getInn())) {
            throw new AlreadyExistsException("Client with INN " + request.getInn() +
                    " already exists");
        }
        Client entity = mapper.mapDtoToEntity(request);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new EntityNotFoundException("Username " + request.getUsername() + " not found"));

        entity.setUser(user);
        repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public List<ClientResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .toList();
    }

    @Override
    public ClientResponse getByUsername(String username) {
        Client client = repository.findByUserUsername(username).orElseThrow(()
                -> new EntityNotFoundException("Client for user " + username + " not found"));
        return mapper.mapEntityToDto(client);
    }

    @Override
    public ClientResponse getById(Long id) {
        Client client = repository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(Client.class, id));
        return mapper.mapEntityToDto(client);
    }

    @Override
    public ClientResponse update(Long id, ClientUpdateRequest updateRequest) {
        Client client = repository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(Client.class, id));

        Optional<Client> existing = repository.findByEmail(updateRequest.getEmail());
        if (existing.isPresent() && (!existing.get().getId().equals(id))) {
            throw new AlreadyExistsException("Client with email " + updateRequest.getEmail()
                    + " already exists");
        }
        if (updateRequest.getInn() != null) {

            Optional<Client> existingInn = repository.findByInn(updateRequest.getInn());
            if (existingInn.isPresent() && (!existingInn.get().getId().equals(id))) {
                throw new AlreadyExistsException("Client with INN " + updateRequest.getInn()
                        + " already exists");
            }
        }
        client.setName(updateRequest.getName());
        client.setAddress(updateRequest.getAddress());
        client.setEmail(updateRequest.getEmail());
        client.setPhone(updateRequest.getPhone());
        client.setClientType(updateRequest.getClientType());
        client.setLegalType(updateRequest.getLegalType());
        client.setInn(updateRequest.getInn());

        repository.save(client);
        return mapper.mapEntityToDto(client);
    }

    @Override
    public void delete(Long id) {
        Client client = repository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(Client.class, id));
        if (orderRepository.existsByClientId(id)) {
            throw new InvalidStatusException("Client with id " + id +
                    " has orders and cannot be deleted");
        }
        repository.delete(client);
    }

}













