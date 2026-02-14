package com.niko.apps.models.habits;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)     // This will remove any null value fields from the final response JSON
public record HabitsResponse(Long id, String name, String description, int currentStreak) {
}
