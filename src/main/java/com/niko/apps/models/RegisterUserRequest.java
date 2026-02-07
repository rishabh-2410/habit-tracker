package com.niko.apps.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterUserRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password must be provided")
    private String password;

    public RegisterUserRequest() {}

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}

