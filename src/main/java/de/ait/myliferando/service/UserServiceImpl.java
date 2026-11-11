package de.ait.myliferando.service;

import de.ait.myliferando.domain.User;
import de.ait.myliferando.dto.mapping.UserMapper;
import de.ait.myliferando.dto.user.UserCreateRequest;
import de.ait.myliferando.dto.user.UserResponse;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.repository.UserRepository;
import de.ait.myliferando.service.interfaces.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;


    public UserServiceImpl(UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;

    }

    @Override
    public UserResponse register(UserCreateRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("Username " + request.getUsername()
                    + " already exists");
        }
        User entity = mapper.mapDtoToEntity(request);
        String hash = encoder.encode(request.getPassword());
        entity.setPassword(hash);
        repository.save(entity);
        return mapper.mapEntityToDto(entity);

    }

    @Override
    public UserResponse getByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(()
                -> new EntityNotFoundException("Username " + username + " not found"));
        return mapper.mapEntityToDto(user);
    }


}
