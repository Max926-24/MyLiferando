package de.ait.myliferando.service.interfaces;


import de.ait.myliferando.dto.courier.CourierCreateRequest;
import de.ait.myliferando.dto.courier.CourierResponse;

import java.util.List;

public interface CourierService {

    CourierResponse save(CourierCreateRequest request);

    List<CourierResponse> findAll();

    CourierResponse startShift(Long id, String username);

    CourierResponse endShift(Long id, String username);
}
