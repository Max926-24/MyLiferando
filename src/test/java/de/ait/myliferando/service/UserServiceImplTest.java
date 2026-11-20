package de.ait.myliferando.service;

import de.ait.myliferando.domain.User;
import de.ait.myliferando.domain.enums.Role;
import de.ait.myliferando.dto.mapping.UserMapperImpl;
import de.ait.myliferando.dto.user.LoginRequest;
import de.ait.myliferando.dto.user.LoginResponse;
import de.ait.myliferando.dto.user.RegisterRequest;
import de.ait.myliferando.dto.user.UserResponse;
import de.ait.myliferando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository repository;
    private PasswordEncoder encoder;
    private UserServiceImpl service;
    private JwtService jwtService;


    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        jwtService = Mockito.mock(JwtService.class);
        service = new UserServiceImpl(repository,
                new UserMapperImpl(), encoder, jwtService);
    }

    @Test
    void registerClient_always_setsClientRole() {
        String username = "client1";
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword("client9234567");

        when(repository.existsByUsername(username)).thenReturn(false);
        when(encoder.encode("client9234567")).thenReturn("hash");

        UserResponse result = service.registerClient(request);
        assertThat(result.getRole()).isEqualTo(Role.CLIENT);
        verify(repository).save(any());
    }

    @Test
    void login_whenCredentialsAreCorrect_returnsToken() {

        User user = new User();
        user.setUsername("dispatcher2");
        user.setPassword("43643456-364765-847873658");
        user.setRole(Role.DISPATCHER);

        LoginRequest request = new LoginRequest();
        request.setUsername("dispatcher2");
        request.setPassword("dispatcher12345");

        when(repository.findByUsername("dispatcher2")).thenReturn(Optional.of(user));
        when(encoder.matches(
                "dispatcher12345",
                "43643456-364765-847873658")).thenReturn(true);
        when(jwtService.generateToken(
                "dispatcher2",
                "DISPATCHER")).thenReturn("token123");

        LoginResponse result = service.login(request);
        assertThat(result.getToken()).isEqualTo("token123");
    }

    @Test
    void login_whenPasswordIsWrong_throwsException() {

        User user = new User();
        user.setUsername("dispatcher2");
        user.setPassword("43643456-364765-847873658");
        user.setRole(Role.DISPATCHER);

        LoginRequest request = new LoginRequest();
        request.setUsername("dispatcher2");
        request.setPassword("dispatcher12345");

        when(repository.findByUsername("dispatcher2")).thenReturn(Optional.of(user));
        when(encoder.matches("dispatcher12345",
                "43643456-364765-847873658")).thenReturn(false);
        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(BadCredentialsException.class);
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void login_whenUserNotFound_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("dispatcher2");
        request.setPassword("dispatcher12345");

        when(repository.findByUsername("dispatcher2"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(BadCredentialsException.class);
        verify(encoder, never()).matches(any(), any());
    }
}
