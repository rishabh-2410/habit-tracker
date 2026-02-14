package com.niko.apps.service;


import com.niko.apps.entity.Habit;
import com.niko.apps.entity.HabitLog;
import com.niko.apps.entity.HabitStatus;
import com.niko.apps.entity.User;
import com.niko.apps.exceptions.DuplicateException;
import com.niko.apps.exceptions.HabitNotFoundException;
import com.niko.apps.models.habits.HabitStats;
import com.niko.apps.repository.HabitLogRepository;
import com.niko.apps.repository.HabitRepository;
import com.niko.apps.repository.UserRepository;

import com.niko.apps.utils.Utils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
public class HabitLogService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final UserRepository userRepository;
    private final Utils utils;


    public HabitLogService(HabitRepository habitRepository, HabitLogRepository habitLogRepository, UserRepository userRepository, Utils utils) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.userRepository = userRepository;
        this.utils = utils;
    }


    public void markHabit(HabitStatus status, Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Habit habit = habitRepository.findHabitByUser_IdAndId(user.getId(), id);

        if (habit == null ) {
            throw new HabitNotFoundException("No valid habit found");
        }

        LocalDate today = LocalDate.now();

        HabitLog log = habitLogRepository.findHabitLogByHabitAndLogDate(habit, today);

        System.out.println("Log from DB: " + log);

        if (log != null) {
            if (log.getStatus() == HabitStatus.DONE && log.getLogDate().equals(today)) {
                throw new DuplicateException("Habit already marked for today");
            }
            log.setStatus(status);
        } else {
            HabitLog newLog = new HabitLog();
            newLog.setLogDate(LocalDate.now());
            newLog.setStatus(status);
            newLog.setHabit(habit);
            habitLogRepository.save(newLog);
        }

    }


    public List<LocalDate> getHabitHistory(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Habit habit = habitRepository.findHabitByUser_IdAndId(user.getId(), id);

        if (habit == null) {
            throw new HabitNotFoundException("No habit found");
        }

        return habitLogRepository.findHabitDatesByHabitId(habit.getId());
    }



    public HabitStats getHabitStats(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Habit habit = habitRepository.findHabitByUser_IdAndId(user.getId(), id);

        if (habit == null) {
            throw new HabitNotFoundException("No habit found");
        }

        List<HabitLog> logs =  habitLogRepository.findHabitLogsByHabit_Id(habit.getId());
        List<LocalDate> completedDates = habitLogRepository.findHabitDatesByHabitId(habit.getId());

        return utils.calculateStats(logs, completedDates, habit.getCreated_at());
    }





}
