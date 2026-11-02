package de.ait.myliferando.service.interfaces;

import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;

import java.util.List;

public interface ClientService {

    ClientResponse save(ClientCreateRequest request);
    List<ClientResponse> findAll();
}
