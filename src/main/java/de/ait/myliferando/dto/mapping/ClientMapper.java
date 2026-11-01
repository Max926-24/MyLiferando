package de.ait.myliferando.dto.mapping;

import de.ait.myliferando.domain.Client;
import de.ait.myliferando.dto.client.ClientCreateRequest;
import de.ait.myliferando.dto.client.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponse mapEntityToDto(Client entity);

    @Mapping(target = "id", ignore = true)
    Client mapDtoToEntity(ClientCreateRequest dto);
}
