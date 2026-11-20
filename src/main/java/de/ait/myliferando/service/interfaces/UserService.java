package de.ait.myliferando.service.interfaces;

import de.ait.myliferando.dto.user.*;

public interface UserService {

    UserResponse register(UserCreateRequest request);

    UserResponse getByUsername(String username);

    UserResponse registerClient(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest request);
}
