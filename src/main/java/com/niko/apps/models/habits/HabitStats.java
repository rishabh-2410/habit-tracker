package com.niko.apps.models.habits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class HabitStats {
    private int daysCompleted;
    private int daysMissed;
    private int longestStreak;
    private int currentStreak;
}
