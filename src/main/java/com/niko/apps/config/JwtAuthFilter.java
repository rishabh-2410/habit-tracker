package com.niko.apps.config;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.niko.apps.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	
	private final JwtService jwtService;

	public JwtAuthFilter(UserDetailsService userDetailsService, JwtService jwtService) {
		super();
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		System.out.println("JWT filter executed");

		String authHeader = request.getHeader("Authorization");
		String token = null; 
		String email = null;
	
		
		// If auth header is present in request and starts with `Bearer `, extract token and the user email from the token
		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			token  = authHeader.substring(7);
			System.out.println("token: " + token);
			email = jwtService.extractEmail(token);
			System.out.println("email from token: " + email);
		}
		
		
		// If user exists and token is valid, convert token to type Authentication so Spring Security can understand it.
		// This sets the Authentication for this request flow only. Any layers in the flow for this particular request
		// can access token details without having to parse the token again and again.
		
		// Check if user is not null and Spring security has not authenticated this request yet.
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			
			// Fetch user from DB 
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			System.out.println("user details from DB: " + userDetails);

			Boolean isValidToken  = jwtService.validateToken(token, userDetails);
			System.out.println("Token validation: " + isValidToken);
			
			// Validate the token and its expiration
			if (jwtService.validateToken(token, userDetails)) {
				
				// If valid token found, create a new sprint security object.
				// This object means the user is not authenticated and has these roles
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());
				
				// Attach request details (metadata like IP, session etc.) to the token.
				// Not mandatory, but good practice
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// Persist user authentication for this request inside Spring security
				// After this, any controller in the flow can get the user details by doing
				// `SecurityContextHolder.getContext().getAuthentication()`
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		// Forward request to the next controller in the flow
		// E.g next() -> express
		filterChain.doFilter(request, response);
		
	}
	
}
