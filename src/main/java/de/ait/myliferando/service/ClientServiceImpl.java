package de.ait.myliferando.service;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.mapping.ClientMapper;
import de.ait.myliferando.repository.ClientRepository;
import de.ait.myliferando.service.interfaces.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ClientResponse save(ClientCreateRequest request) {
        Client entity = mapper.mapDtoToEntity(request);
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
}
