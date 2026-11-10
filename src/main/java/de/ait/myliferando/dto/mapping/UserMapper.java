package de.ait.myliferando.dto.mapping;

import de.ait.myliferando.domain.User;
import de.ait.myliferando.dto.user.UserCreateRequest;
import de.ait.myliferando.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse mapEntityToDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User mapDtoToEntity(UserCreateRequest dto);
}
