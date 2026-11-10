package de.ait.myliferando.service.interfaces;

import de.ait.myliferando.dto.user.UserCreateRequest;
import de.ait.myliferando.dto.user.UserResponse;

public interface UserService {

    UserResponse register(UserCreateRequest request);
}
