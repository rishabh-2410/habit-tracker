package com.niko.apps.controllers.auth;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "user")
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@JsonProperty("email")
	@Email(message = "Invalid email format")     // Check email format. Rejects inavlid strings like `abc`, `@gmail`, `abc@` etc.
	@NotBlank(message = "Email cannot be empty") // Check if email is empty in incoming request.
	@Column(nullable = false, unique = true)	 // Values in DB for this column must be unique and null value is not allowed.
	private String email;
	
	@JsonIgnore
	private String password;
	
	@JsonProperty("created_at")
	@Column(nullable = false, updatable = false)
	private LocalDateTime created_at;
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}



	public User(Long id, String email, String password, LocalDateTime created_at) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.created_at = created_at;
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
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", created_at=" + created_at + "]";
	}
	
	
	
}
