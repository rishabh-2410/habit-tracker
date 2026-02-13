package com.niko.apps.repository;


import com.niko.apps.entity.Habit;
import com.niko.apps.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    @Query("""
           SELECT h.logDate
           FROM habit_log h
           WHERE h.habit.id = :habitId
           ORDER BY h.logDate DESC
           """)
    List<LocalDateTime> findHabitDatesByHabitId(@Param("habitId") Long id);

    HabitLog findHabitLogsByHabit_Id(Long habitId);

    HabitLog findHabitLogsByHabit_IdAndLogDate(Long habitId, LocalDate logDate);

    HabitLog findHabitLogByHabitAndLogDate(Habit habit, LocalDate logDate);
}
