package com.niko.apps.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password must be provided")
    private String password;

    public RegisterRequest() {}

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}

