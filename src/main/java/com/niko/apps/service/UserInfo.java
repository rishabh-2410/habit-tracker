package com.niko.apps.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.niko.apps.entity.User;

@SuppressWarnings("serial")
public class UserInfo implements UserDetails {
	
	private String email;
	private String password;
	private List<GrantedAuthority> authorities;
	public UserInfo(User user) {
		super();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.authorities = List.of(user.getRoles().split(","))
						    .stream()
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
