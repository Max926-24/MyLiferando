package de.ait.myliferando.service;

import de.ait.myliferando.domain.Courier;
import de.ait.myliferando.domain.enums.CourierStatus;
import de.ait.myliferando.dto.courier.CourierCreateRequest;
import de.ait.myliferando.dto.courier.CourierResponse;
import de.ait.myliferando.dto.mapping.CourierMapper;
import de.ait.myliferando.repository.CourierRepository;
import de.ait.myliferando.service.interfaces.CourierService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository repository;
    private final CourierMapper mapper;

    public CourierServiceImpl(CourierRepository repository, CourierMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public CourierResponse save(CourierCreateRequest request) {
        Courier entity = mapper.mapDtoToEntity(request);
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
    public CourierResponse startShift(Long id) {
        Courier courier = repository.findById(id).orElseThrow();
        courier.setStatus(CourierStatus.AVAILABLE);
        repository.save(courier);
        return mapper.mapEntityToDto(courier);
    }

    @Override
    public CourierResponse endShift(Long id) {
        Courier courier = repository.findById(id).orElseThrow();
        courier.setStatus(CourierStatus.OFF_DUTY);
        repository.save(courier);
        return mapper.mapEntityToDto(courier);

    }
}
