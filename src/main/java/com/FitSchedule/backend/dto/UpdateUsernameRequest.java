package com.FitSchedule.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUsernameRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "New username cannot be blank")
    @Size(min = 2, max = 100, message = "Username must be between 2 and 100 characters")
    private String newUsername;
}
