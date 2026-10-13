package com.FitSchedule.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "user_email", length = 120)
    private String userEmail;

    @Column(name = "fitness_goal", nullable = false, length = 60)
    private String fitnessGoal;

    @Column(name = "experience_level", nullable = false, length = 30)
    private String experienceLevel;

    @Column(name = "workout_frequency", nullable = false)
    private Integer workoutFrequency;

    @Column(name = "workout_duration", nullable = false)
    private Integer workoutDuration;

    @Column(nullable = false, length = 60)
    private String equipment;

    @Column(name = "training_split", nullable = false, length = 60)
    private String trainingSplit;

    @Column(name = "target_muscle_groups", length = 255)
    private String targetMuscleGroups;

    @Column(name = "exercises_to_avoid", length = 255)
    private String exercisesToAvoid;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayOrder ASC")
    @JsonManagedReference
    @Builder.Default
    private List<WorkoutDay> days = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addDay(WorkoutDay day) {
        days.add(day);
        day.setWorkoutPlan(this);
    }

    public void removeDay(WorkoutDay day) {
        days.remove(day);
        day.setWorkoutPlan(null);
    }
}
