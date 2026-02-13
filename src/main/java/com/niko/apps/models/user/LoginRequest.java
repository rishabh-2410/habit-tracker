package com.niko.apps.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email cannot be empty")
	private String email;

	@NotBlank(message = "Password must be provided")
	private String password;

	public LoginRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public LoginRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
