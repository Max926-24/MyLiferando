package de.ait.myliferando.service.interfaces;

import de.ait.myliferando.dto.user.RegisterRequest;
import de.ait.myliferando.dto.user.UserCreateRequest;
import de.ait.myliferando.dto.user.UserResponse;

public interface UserService {

    UserResponse register(UserCreateRequest request);

    UserResponse getByUsername(String username);

    UserResponse registerClient(RegisterRequest registerRequest);
}
