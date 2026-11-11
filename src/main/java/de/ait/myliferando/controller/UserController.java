package de.ait.myliferando.controller;

import de.ait.myliferando.dto.user.UserResponse;
import de.ait.myliferando.service.interfaces.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}
