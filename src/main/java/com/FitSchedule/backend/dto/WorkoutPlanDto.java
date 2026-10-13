package com.FitSchedule.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanDto {
    private Long id;
    private String title;
    private String userEmail;
    private String fitnessGoal;
    private String experienceLevel;
    private Integer workoutFrequency;
    private Integer workoutDuration;
    private String equipment;
    private String trainingSplit;
    private String targetMuscleGroups;
    private String exercisesToAvoid;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private List<WorkoutDayDto> days = new ArrayList<>();
}
