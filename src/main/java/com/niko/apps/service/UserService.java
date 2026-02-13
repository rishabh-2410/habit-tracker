package com.niko.apps.service;

import java.util.Optional;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.niko.apps.entity.User;
import com.niko.apps.exceptions.DuplicateException;
import com.niko.apps.models.user.RegisterRequest;
import com.niko.apps.repository.UserRepository;
import static com.niko.apps.constants.AppConstants.*;

@Component
public class UserService implements UserDetailsService{
	
	private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
  
    
    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder,JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        
    }
    
    // Method to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	// Filter and fetch user details and store in UserInfo type object (Actual user entity in DB)
    	Optional<User> userFromDB = repository.findByEmail(email);
    	
    	// Check whether user is empty or not
    	if ( userFromDB.isEmpty()) {
    		throw new UsernameNotFoundException(USER_NOT_FOUND_MSG + email);
    	}
    	
    	
    	// Create another object of the same user entity type (Type that is stored in DB)
    	User localUser = userFromDB.get();
    	
    	// Return UserInfoDetails type of object passing the entity typed object
    	// Internally, UserInfoDetails's constructor will extract email, password and roles into its object
    	// And since UserInfoDetails implements UserDetails, this is a valid return matching the return type of the function.
    	return new UserInfo(localUser);
    }
    
    
    
    // Register user
    public void registerUser(RegisterRequest req) {
    	// Check if user already exists
    	repository.findByEmail(req.getEmail())
    		     .ifPresent(u -> {
            throw new DuplicateException(DUPLICATE_USER_MSG);
        });

    	// Create new user entity
    	User user = new User();
    	
    	// Set email from request
    	user.setEmail(req.getEmail());
    	
    	
    	// Set role for the user
    	user.setRole(ROLE_USER);
    	
        // Encrypt password before saving
        user.setPassword(encoder.encode(req.getPassword())); 
        repository.save(user);
        
    }
    
    
    // Register user
    public String loginUser(String email) {
        return jwtService.generateToken(email);
    }
    
}
