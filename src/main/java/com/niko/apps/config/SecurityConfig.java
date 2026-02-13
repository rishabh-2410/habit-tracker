package com.niko.apps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		super();
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder =  passwordEncoder;
	}
	
	/* 
     * Main security configuration
     * Defines endpoint access rules and JWT filter setup
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// Disable CSRF (Not needed for stateless JWT)
		http.csrf(AbstractHttpConfigurer::disable)
				//Stateless session (required for JWT)
		     .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Configure endpoint authorization
		     .authorizeHttpRequests(auth -> auth
											// Public endpoints 
											.requestMatchers("/auth/api/v1/register", "/auth/api/v1/login").permitAll()
											
//											// Role-based endpoints
//											.requestMatchers("/auth/user/").hasAuthority("ROLE_USER")
//											.requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")
											
											// All other endpoints
											.anyRequest().authenticated()
									)
				//Set custom authentication header
			 .authenticationProvider(authenticationProvider())
				//Add JWT filter before Spring Security's default filter
		     .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}


	/* 
     * Authentication provider configuration
     * Links UserDetailsService and PasswordEncoder
     */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
	

    /* 
     * Authentication manager bean
     * Required for programmatic authentication
     */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
