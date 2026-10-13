package com.FitSchedule.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trainer_inquiries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 120)
    private String email;

    @Column(name = "fitness_goal", length = 100)
    private String fitnessGoal;

    @NotBlank(message = "Message or question is required")
    @Size(min = 5, max = 2000, message = "Message must be between 5 and 2000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "trainer_id")
    private Long trainerId;

    @Column(name = "trainer_name", length = 100)
    private String trainerName;

    @Column(length = 30)
    @Builder.Default
    private String status = "SUBMITTED";

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "SUBMITTED";
        }
        if (this.fullName == null || this.fullName.trim().isEmpty()) {
            this.fullName = "Member Client";
        }
        if (this.email == null || this.email.trim().isEmpty()) {
            this.email = "member@fitschedule.com";
        }
        if (this.fitnessGoal == null || this.fitnessGoal.trim().isEmpty()) {
            this.fitnessGoal = "General Consultation";
        }
    }
}
