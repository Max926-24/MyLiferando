package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.domain.User;
import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.mapping.ClientMapper;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.repository.UserRepository;
import de.ait.myliferando.service.interfaces.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final UserRepository userRepository;

    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
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
}
