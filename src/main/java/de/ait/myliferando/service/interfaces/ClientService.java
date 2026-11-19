package de.ait.myliferando.service.interfaces;

import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import de.ait.myliferando.dto.client.ClientUpdateRequest;

import java.util.List;

public interface ClientService {

    ClientResponse save(ClientCreateRequest request);

    List<ClientResponse> findAll();

    ClientResponse getByUsername(String username);

    ClientResponse getById(Long id);

    ClientResponse update(Long id, ClientUpdateRequest updateRequest);

    void delete(Long id);
}
