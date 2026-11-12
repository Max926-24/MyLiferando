package de.ait.myliferando.controller;

import de.ait.myliferando.dto.user.UserCreateRequest;
import de.ait.myliferando.dto.user.UserResponse;
import de.ait.myliferando.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        return service.getByUsername(principal.getName());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return service.register(request);

    }
}
