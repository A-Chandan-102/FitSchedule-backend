package com.FitSchedule.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDayDto {
    private Long id;
    private String dayOfWeek;
    private Integer dayOrder;
    private String title;
    private String focus;
    private Integer estimatedDuration;
    private Boolean isRestDay;
    
    @Builder.Default
    private List<WorkoutDayExerciseDto> exercises = new ArrayList<>();
}
