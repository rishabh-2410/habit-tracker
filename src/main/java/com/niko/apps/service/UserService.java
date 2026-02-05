package com.niko.apps.service;

import java.util.Optional;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.niko.apps.entity.User;
import com.niko.apps.repository.UserRepository;


@Component
public class UserService implements UserDetailsService{
	
	private final UserRepository repository;
    private final PasswordEncoder encoder;
    
    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }
    
    // Method to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	// Filter and fetch user details and store in UserInfo type object (Actual user entity in DB)
    	Optional<User> userFromDB = repository.findByEmail(email);
    	
    	// Check whether user is empty or not
    	if ( userFromDB.isEmpty()) {
    		throw new UsernameNotFoundException("User not found with email: " + email);
    	}
    	
    	
    	// Create another object of the same user entity type (Type that is stored in DB)
    	User localUser = userFromDB.get();
    	
    	// Return UserInfoDetails type of object passing the entity typed object
    	// Internally, UserInfoDetails's constructor will extract email, password and roles into its object
    	// And since UserInfoDetails implements UserDetails, this is a valid return mactching the return type of the function.
    	return new UserInfo(localUser);
    }
    
    
    // TODO Add proper validations for incoming requests 
    
    // Add any additional methods for registering or managing users
    public String addUser(User user) {
        // Encrypt password before saving
        user.setPassword(encoder.encode(user.getPassword())); 
        repository.save(user);
        return "User added successfully!";
    }
    
    
    // TODO Add other authentication methods
  
}
