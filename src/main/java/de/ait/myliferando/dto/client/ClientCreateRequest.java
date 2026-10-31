package de.ait.myliferando.dto.client;

import de.ait.myliferando.domain.enums.ClientType;
import de.ait.myliferando.domain.enums.LegalType;
import jakarta.validation.constraints.*;

public class ClientCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotNull
    private ClientType clientType;

    @NotNull
    private LegalType legalType;

    @Pattern(regexp = "\\d{10}|\\d{12}")
    private String inn;


    public ClientCreateRequest() {
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



