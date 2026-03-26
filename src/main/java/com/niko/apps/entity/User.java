package com.niko.apps.entity;

import java.time.LocalDateTime;


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
import lombok.Getter;
import lombok.Setter;


@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false, unique = true)	 // Values in DB for this column must be unique and null value is not allowed.
	private String email;

	@Column(nullable = false) // Values in DB for this column cannot be null
	private String fullName;

	@Column(nullable = false) // Values in DB for this column cannot be null
	private String password;
	
	@JsonProperty("created_at")
	@Column(nullable = false, updatable = false)
	private LocalDateTime created_at;
	
	
	@Column(nullable = false, updatable = false)
	private String role;
	

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public User(Long id,
				@Email(message = "Invalid email format") 
				@NotBlank(message = "Email cannot be empty") 
				String email,
				String fullName,
				String password, 
				LocalDateTime created_at, 
				String role
	) {
		super();
		this.id = id;
		this.email = email;
		this.fullName = fullName;
		this.password = password;
		this.created_at = created_at;
		this.role = role;
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
