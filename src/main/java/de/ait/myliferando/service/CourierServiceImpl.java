package de.ait.myliferando.service;

import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.User;
import de.ait.myliferando.domain.enums.CourierStatus;
import de.ait.myliferando.dto.courier.CourierCreateRequest;
import de.ait.myliferando.dto.courier.CourierResponse;
import de.ait.myliferando.dto.mapping.CourierMapper;
import de.ait.myliferando.exceptions.types.AlreadyExistsException;
import de.ait.myliferando.exceptions.types.EntityNotFoundException;
import de.ait.myliferando.exceptions.types.InvalidStatusException;
import de.ait.myliferando.repository.CourierRepository;
import de.ait.myliferando.repository.UserRepository;
import de.ait.myliferando.service.interfaces.CourierService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository repository;
    private final CourierMapper mapper;
    private final UserRepository userRepository;

    public CourierServiceImpl(CourierRepository repository, CourierMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    @Override
    public CourierResponse save(CourierCreateRequest request) {
        if (repository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException("Courier with phone number " + request.getPhone() +
                    " already exists");
        }
        Courier entity = mapper.mapDtoToEntity(request);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Username "
                        + request.getUsername() + " not found"));
        entity.setUser(user);
        repository.save(entity);
        return mapper.mapEntityToDto(entity);
    }

    @Override
    public List<CourierResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .toList();
    }

    @Override
    public CourierResponse startShift(Long id, String username) {
        Courier courier = repository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(Courier.class, id));
        if (courier.getStatus() != CourierStatus.OFF_DUTY) {
            throw new InvalidStatusException("Courier: " + id + " is already on shift");
        }
        checkIsOwnAccount(courier, username);
        courier.setStatus(CourierStatus.AVAILABLE);
        repository.save(courier);
        return mapper.mapEntityToDto(courier);
    }

    @Override
    public CourierResponse endShift(Long id, String username) {
        Courier courier = repository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(Courier.class, id));
        if (courier.getStatus() != CourierStatus.AVAILABLE) {
            throw new InvalidStatusException("Courier: " + id +
                    " cannot end shift in status " + courier.getStatus());
        }
        checkIsOwnAccount(courier, username);
        courier.setStatus(CourierStatus.OFF_DUTY);
        repository.save(courier);
        return mapper.mapEntityToDto(courier);
    }

    private void checkIsOwnAccount(Courier courier, String username) {
        User courierUser = courier.getUser();
        if (courierUser == null || !courierUser.getUsername().equals(username)) {
            throw new AccessDeniedException(
                    "Courier " + courier.getId() + " is not your account");
        }
    }
}
