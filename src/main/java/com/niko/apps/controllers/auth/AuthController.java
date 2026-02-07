package com.niko.apps.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.niko.apps.models.AuthRequest;
import com.niko.apps.models.RegisterUserRequest;
import com.niko.apps.service.JwtService;
import com.niko.apps.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private UserService authService;
	private JwtService jwtService;
	
	private AuthenticationManager authenticationManager;
	

	public AuthController(UserService authService, JwtService jwtService, AuthenticationManager authenticationManager) {
		super();
		this.authService = authService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome, this endpoint is not secure";
	}
	
	@PostMapping("/api/v1/register")
	public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterUserRequest req) {
		authService.register(req);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	@PostMapping("/generateToken")
	public String loginUser(@Valid @RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
				);
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getEmail());
		} else {
			throw new UsernameNotFoundException("Invalid user request");
		}
	}
	
	
	@GetMapping("/user/profile")
	public String userRoleHandler() {
		return "Welcome to user profile";
	}
	
	
	@GetMapping("/user/admin")
	public String adminRoleHandler() {
		return "Welcome to admin profile";
	}
	
}
