package com.niko.apps.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message="Name cannot be empty")
    private String fullName;

    @NotBlank(message = "Password must be provided")
    private String password;

    public RegisterRequest() {}

}

