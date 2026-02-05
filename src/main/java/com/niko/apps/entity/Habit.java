package com.niko.apps.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "habit")
@Table(name = "habits")
public class Habit {

	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@JsonIgnore()
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("desc")
	private String description;
	
	@JsonProperty("created_at")
	private LocalDateTime created_at;
	
	
}
