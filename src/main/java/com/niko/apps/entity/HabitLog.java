package com.niko.apps.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Data
@Getter
@Setter
@Entity(name = "habit_log")
@Table(name = "habit_log", uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "date"}))
public class HabitLog {

    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonIgnore()
    @JoinColumn(name = "habit_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Habit habit;

    @JsonProperty("log_date")
    private LocalDate logDate;


    @Enumerated(EnumType.STRING)
    private HabitStatus status;

}
