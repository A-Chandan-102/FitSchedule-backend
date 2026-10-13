package com.FitSchedule.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDayExerciseDto {
    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;
    private String subCategory;
    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private Integer orderIndex;
    private String notes;
}
