package com.FitSchedule.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmailRequest {

    @NotBlank(message = "Current email is required")
    private String currentEmail;

    @NotBlank(message = "New email is required")
    @Email(message = "Please provide a valid email address")
    private String newEmail;
}
