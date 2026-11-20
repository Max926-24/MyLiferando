package de.ait.myliferando.controller;

import de.ait.myliferando.dto.user.LoginRequest;
import de.ait.myliferando.dto.user.LoginResponse;
import de.ait.myliferando.dto.user.RegisterRequest;

import de.ait.myliferando.dto.user.UserResponse;
import de.ait.myliferando.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;


    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return service.registerClient(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

}
