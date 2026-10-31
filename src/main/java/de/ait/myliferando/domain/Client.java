package de.ait.myliferando.domain;

import de.ait.myliferando.domain.enums.ClientType;
import de.ait.myliferando.domain.enums.LegalType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(max = 255)
    @NotBlank
    private String name;

    @Column(name = "address", nullable = false)
    @Size(max = 255)
    @NotBlank
    private String address;

    @Column(name = "email", nullable = false, unique = true)
    @Size(max = 255)
    @Email
    @NotBlank
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    @Size(max = 20)
    @NotBlank
    private String phone;

    @Column(name = "client_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ClientType clientType;

    @Column(name = "legal_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private LegalType legalType;

    @Column(name = "inn", unique = true, length = 12)
    @Pattern(regexp = "\\d{10}|\\d{12}")
    private String inn;

    public Client() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client client)) {
            return false;
        }
        return id != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", clientType=" + clientType +
                ", legalType=" + legalType +
                ", inn='" + inn + '\'' +
                '}';
    }
}

