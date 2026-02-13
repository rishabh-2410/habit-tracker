package com.niko.apps.models.habits;


import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddHabitRequest(String name, String description) {
}
