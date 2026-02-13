package com.niko.apps.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
	}
	
}
