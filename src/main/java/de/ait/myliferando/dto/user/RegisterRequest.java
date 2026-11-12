package de.ait.myliferando.dto.user;


import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(min = 10, max = 30)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


