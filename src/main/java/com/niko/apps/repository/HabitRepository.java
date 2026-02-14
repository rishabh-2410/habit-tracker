package com.niko.apps.repository;

import com.niko.apps.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.niko.apps.entity.Habit;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser_Id(Long id);

    Habit findHabitById(Long id);

    Habit findHabitByUser_IdAndId(Long userId, Long id);

    Habit findHabitByUser(User user);
}
