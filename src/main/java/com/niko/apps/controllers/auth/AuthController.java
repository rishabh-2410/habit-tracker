package com.niko.apps.controllers.auth;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niko.apps.models.user.LoginRequest;
import com.niko.apps.models.user.LoginResponse;
import com.niko.apps.models.user.RegisterRequest;

import com.niko.apps.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final UserService authService;
	  private final AuthenticationManager authenticationManager;
	

	public AuthController(UserService authService, AuthenticationManager authenticationManager) {
		super();
		this.authService = authService;
		this.authenticationManager = authenticationManager;
	}


	@PostMapping("/api/v1/register")
	public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterRequest req) {
		authService.registerUser(req);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	@PostMapping("/api/v1/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest req) {
	 	// Create a new authentication object using `UsernamePasswordAuthenticationToken` 
		// and validate the username and password
		Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
					);
		
			String token = authService.loginUser(req.getEmail());
			return ResponseEntity.ok(new LoginResponse(token));

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
