package com.niko.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niko.apps.entity.Habit;

public interface HabitRepository extends JpaRepository<Habit, Long> {

}
