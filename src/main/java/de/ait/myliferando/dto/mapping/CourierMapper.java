package de.ait.myliferando.dto.mapping;

import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.dto.courier.CourierCreateRequest;
import de.ait.myliferando.dto.courier.CourierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourierMapper {

    CourierResponse mapEntityToDto(Courier entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Courier mapDtoToEntity(CourierCreateRequest dto);
}
