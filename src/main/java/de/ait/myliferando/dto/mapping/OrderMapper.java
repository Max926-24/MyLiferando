package de.ait.myliferando.dto.mapping;

import de.ait.myliferando.domain.Order;
import de.ait.myliferando.dto.order.OrderCreateRequest;
import de.ait.myliferando.dto.order.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "courier.id", target = "courierId")
    @Mapping(source = "courier.name", target = "courierName")
    OrderResponse mapEntityToDto(Order entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    @Mapping(target = "courier", ignore = true)
    @Mapping(target = "client", ignore = true)
    Order mapDtoToEntity(OrderCreateRequest dto);
}
