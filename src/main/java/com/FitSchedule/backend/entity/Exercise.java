package com.FitSchedule.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "muscle_group", nullable = false, length = 60)
    private String muscleGroup;

    @Column(name = "sub_category", length = 80)
    private String subCategory;

    @Column(length = 60)
    private String category; // e.g. Compound, Isolation, Cardio

    @Column(nullable = false, length = 60)
    private String equipment; // e.g. Full gym, Dumbbells, Resistance bands, Bodyweight only

    @Column(name = "experience_level", length = 30)
    private String experienceLevel; // e.g. Beginner, Intermediate, Advanced, All

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_sets")
    private Integer defaultSets;

    @Column(name = "default_reps", length = 30)
    private String defaultReps;

    @Column(name = "default_rest_seconds")
    private Integer defaultRestSeconds;
}
