package com.FitSchedule.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonBackReference
    private WorkoutPlan workoutPlan;

    @Column(name = "day_of_week", nullable = false, length = 30)
    private String dayOfWeek; // e.g. Monday, Tuesday, etc.

    @Column(name = "day_order", nullable = false)
    private Integer dayOrder; // 1 to 7

    @Column(nullable = false, length = 100)
    private String title; // e.g. Upper Body Focus

    @Column(nullable = false, length = 100)
    private String focus; // e.g. Chest & Triceps, Full Body, Rest

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // in minutes

    @Column(name = "is_rest_day", nullable = false)
    @Builder.Default
    private Boolean isRestDay = false;

    @OneToMany(mappedBy = "workoutDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    @Builder.Default
    private List<WorkoutDayExercise> exercises = new ArrayList<>();

    public void addExercise(WorkoutDayExercise exercise) {
        exercises.add(exercise);
        exercise.setWorkoutDay(this);
    }

    public void removeExercise(WorkoutDayExercise exercise) {
        exercises.remove(exercise);
        exercise.setWorkoutDay(null);
    }
}
