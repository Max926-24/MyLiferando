package de.ait.myliferando.service;

import de.ait.myliferando.domain.enums.Role;
import de.ait.myliferando.dto.mapping.UserMapperImpl;
import de.ait.myliferando.dto.user.RegisterRequest;
import de.ait.myliferando.dto.user.UserResponse;
import de.ait.myliferando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserRepository repository;
    private PasswordEncoder encoder;
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        service = new UserServiceImpl(repository, new UserMapperImpl(), encoder);
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
}
