package com.FitSchedule.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String specialization;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 120)
    private String email;

    @Column(length = 100)
    private String password;

    private Double rating;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = true;
}
