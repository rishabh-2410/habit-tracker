package com.niko.apps.utils;

import com.niko.apps.entity.HabitLog;
import com.niko.apps.models.habits.HabitStats;
import com.niko.apps.repository.HabitLogRepository;
import lombok.Data;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
public class Utils {


    public int calculateCurrentStreak(List<LocalDate> completedDates) {
        if (completedDates == null || completedDates.isEmpty()) {
            return 0;
        }

        LocalDate today = LocalDate.now();

        // Determine where streak should start
        LocalDate startDate;

        if (completedDates.get(0).equals(today)) {
            startDate = today;
        } else {
            startDate = today.minusDays(1);
        }


        int streak = 0;


        // For every date in completedDates (sorted in descending order),
        // check if the date matches today minus the current streak count
        // e.g. date = today - streak
        // If it does, increment streak.
        // If we find a gap (a missing day), stop counting.
        for (LocalDate date : completedDates) {
            if (date.equals(startDate.minusDays(streak))) {
                streak++;
            } else {
                break;
            }
        }

        // return the current active streak
        return streak;
    }


    public int calculateLongestStreak(List<LocalDate> completedDates) {
        if (completedDates == null || completedDates.isEmpty()) {
            return 0;
        }

        // Sort the dates
        completedDates.sort(LocalDate::compareTo);

        int longestStreak = 1;
        int currentStreak = 1;


        for (int i = 1; i < completedDates.size(); i++ ) {
            LocalDate previous = completedDates.get(i - 1);
            LocalDate current = completedDates.get(i);


            if (current.equals(previous.plusDays(1))) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }

            longestStreak = Math.max(longestStreak, currentStreak);
        }

        return longestStreak;
    }


    public HabitStats calculateStats(List<HabitLog> logs, List<LocalDate> completedDates, LocalDateTime createdAt) {
        long totality = ChronoUnit.DAYS.between(createdAt.toLocalDate(), LocalDate.now()) + 1;

        int missedDays = (int) (totality - completedDates.size());

        int currentStreak = calculateCurrentStreak(completedDates);

        int longestStreak = calculateLongestStreak(completedDates);

        return new HabitStats(completedDates.size(), missedDays, longestStreak, currentStreak);
    }
}
