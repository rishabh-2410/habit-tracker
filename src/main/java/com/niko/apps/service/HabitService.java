package com.niko.apps.service;

import com.niko.apps.entity.Habit;
import com.niko.apps.entity.User;
import com.niko.apps.models.habits.HabitsResponse;
import com.niko.apps.repository.HabitLogRepository;
import com.niko.apps.repository.HabitRepository;
import com.niko.apps.repository.UserRepository;
import com.niko.apps.utils.Utils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final UserRepository userRepository;
    private final Utils utils;

    public HabitService(HabitRepository habitRepository, HabitLogRepository habitLogRepository, UserRepository userRepository, Utils utils) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.userRepository = userRepository;
        this.utils = utils;
    }


    public List<HabitsResponse> getHabitsForCurrentUser(String email) {

        // Get user from DB based on email, to get the user_id from the DB
        User user = userRepository.findByEmail(email).orElseThrow();

        System.out.println("User from DB: " + user);

        // Get habits for user using user_id
        List<Habit> habits = habitRepository.findByUser_Id(user.getId());

        System.out.println("Habits of a user from DB: " + habits);

        // For each habit, calculate the current active streak and
        // create a habitResponse object.
        return habits.stream().map(habit -> {
                int streak = calculateCurrentStreak(habit.getId());
                return new HabitsResponse(
                        habit.getId(),
                        habit.getName(),
                        habit.getDescription(),
                        streak
                );
        }).toList();
    }

    public int calculateCurrentStreak(Long habitId) {
        // Get all completed dates from DB (habit_log)
        List<LocalDate> completedDates = habitLogRepository.findHabitDatesByHabitId(habitId);

        return utils.calculateCurrentStreak(completedDates);
    }


    public void addHabit(String name, String description, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Habit habit = new Habit();
        habit.setName(name);
        habit.setDescription(description);
        habit.setUser(user);
        habitRepository.save(habit);
    }


}
