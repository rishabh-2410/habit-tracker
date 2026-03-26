package com.niko.apps.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.niko.apps.entity.User;

@NullMarked
public class UserInfo implements UserDetails {
	
	private final String email;
	private final String password;
	private final List<GrantedAuthority> authorities;
	public UserInfo(User user) {
		super();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.authorities = Stream.of(user.getRole().split(","))
						    .map(role -> new SimpleGrantedAuthority("ROLE_"+ role))
						    .collect(Collectors.toList());
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
	
}
