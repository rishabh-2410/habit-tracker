package com.niko.apps.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity(name = "user")
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false, unique = true)	 // Values in DB for this column must be unique and null value is not allowed.
	private String email;
	
	@Column(nullable = false) // Values in DB for this column cannot be null
	private String password;
	
	@JsonProperty("created_at")
	@Column(nullable = false, updatable = false)
	private LocalDateTime created_at;
	
	
	@Column(nullable = false, updatable = false)
	private String role;
	
	
	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}



	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public User(Long id,
				@Email(message = "Invalid email format") 
				@NotBlank(message = "Email cannot be empty") 
				String email,
				String password, 
				LocalDateTime created_at, 
				String role
	) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.created_at = created_at;
		this.role = role;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public LocalDateTime getCreated_at() {
		return created_at;
	}



	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", created_at=" + created_at
				+ ", role=" + role + "]";
	}
	
	@PrePersist
	public void prePersist() {
	    this.created_at = LocalDateTime.now();
	}
	
}
