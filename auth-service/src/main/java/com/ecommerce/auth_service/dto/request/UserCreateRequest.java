package com.ecommerce.auth_service.dto.request;

import java.time.LocalDate;

public class UserCreateRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
