package de.ait.myliferando.dto.courier;

import de.ait.myliferando.domain.enums.CourierStatus;
import de.ait.myliferando.domain.enums.Transport;

public class CourierResponse {

    private Long id;
    private String name;
    private String phone;
    private CourierStatus status;
    private Transport transport;

    public CourierResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CourierStatus getStatus() {
        return status;
    }

    public void setStatus(CourierStatus status) {
        this.status = status;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
