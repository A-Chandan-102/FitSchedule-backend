package com.FitSchedule.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutGenerateRequest {

    private String planTitle;

    @NotBlank(message = "Fitness goal is required")
    private String fitnessGoal;

    @NotBlank(message = "Experience level is required")
    private String experienceLevel;

    @NotNull(message = "Workout frequency is required")
    @Min(value = 2, message = "Workout frequency must be at least 2 days per week")
    @Max(value = 6, message = "Workout frequency must not exceed 6 days per week")
    private Integer workoutFrequency;

    @NotEmpty(message = "Preferred training days are required")
    private List<String> preferredDays;

    @NotNull(message = "Workout duration is required")
    private Integer workoutDuration;

    @NotBlank(message = "Available equipment is required")
    private String equipment;

    private String trainingSplit; // Full body, Upper/lower, Push/pull/legs, or Auto

    private String targetMuscleGroups;

    private String exercisesToAvoid;

    private String notes;
}
