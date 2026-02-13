package com.niko.apps.entity;


public class AuthRequest {
	
	private String email;
	
	private String password;

	public AuthRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public AuthRequest() {
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
