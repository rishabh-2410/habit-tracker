package com.niko.apps.controllers.tracking;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.niko.apps.controllers.habits.Habit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "habit_log")
@Table( name = "habit_log", uniqueConstraints = @UniqueConstraint(columnNames= {"habit_id", "date"}))
public class HabitLog {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@JsonIgnore()
	@JoinColumn(name = "habit_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Habit habit;
	
	@JsonProperty("log_date")
	private LocalDateTime logDate;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private HabitStatus status;
	
}
