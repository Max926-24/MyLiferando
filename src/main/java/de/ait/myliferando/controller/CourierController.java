package de.ait.myliferando.controller;

import de.ait.myliferando.dto.courier.CourierCreateRequest;
import de.ait.myliferando.dto.courier.CourierResponse;
import de.ait.myliferando.service.interfaces.CourierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/couriers")
public class CourierController {

    private final CourierService service;

    public CourierController(CourierService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourierResponse create(@Valid @RequestBody CourierCreateRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<CourierResponse> findAll() {
        return service.findAll();
    }

    @PatchMapping("/{id}/start-shift")
    public CourierResponse startShift(@PathVariable Long id, Principal principal) {
        return service.startShift(id, principal.getName());
    }

    @PatchMapping("/{id}/end-shift")
    public CourierResponse endShift(@PathVariable Long id, Principal principal) {
        return service.endShift(id, principal.getName());
    }
}
