package de.ait.myliferando.dto.client;

import de.ait.myliferando.domain.enums.ClientType;
import de.ait.myliferando.domain.enums.LegalType;


public class ClientResponse {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private ClientType clientType;
    private LegalType legalType;
    private String inn;

    public ClientResponse() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public LegalType getLegalType() {
        return legalType;
    }

    public void setLegalType(LegalType legalType) {
        this.legalType = legalType;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }
}
