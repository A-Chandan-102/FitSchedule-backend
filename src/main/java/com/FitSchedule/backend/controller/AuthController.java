package com.FitSchedule.backend.controller;

import com.FitSchedule.backend.dto.*;
import com.FitSchedule.backend.entity.Trainer;
import com.FitSchedule.backend.entity.User;
import com.FitSchedule.backend.exception.BadRequestException;
import com.FitSchedule.backend.repository.TrainerRepository;
import com.FitSchedule.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    // ─── Login ────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid email or password.");
        }

        // Unverified trainers are not permitted to log in - they must be added to the database by an administrator
        if ("TRAINER".equalsIgnoreCase(user.getRole())) {
            var trainerOpt = trainerRepository.findByEmailIgnoreCase(user.getEmail());
            if (trainerOpt.isEmpty()) {
                throw new BadRequestException("Unverified trainer account. Trainers must be added to the database by an administrator before logging in.");
            }
            Trainer trainer = trainerOpt.get();
            if (trainer.getIsVerified() != null && !trainer.getIsVerified()) {
                throw new BadRequestException("This trainer account is unverified. Please contact an administrator.");
            }
        }

        String token = "FS-" + UUID.randomUUID().toString();

        AuthResponse response = AuthResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .specialization(user.getSpecialization())
                .token(token)
                .message("Login successful")
                .build();

        return ResponseEntity.ok(response);
    }

    // ─── Register (Members only – trainers are added by admin via database) ──

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Trainers cannot self-register; they must be added via the admin panel
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("TRAINER")) {
            throw new BadRequestException("Trainer accounts cannot be created via sign-up. Trainers must be added by an administrator.");
        }

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .password(request.getPassword())
                .role("MEMBER")
                .build();

        User saved = userRepository.save(user);
        String token = "FS-" + UUID.randomUUID().toString();

        AuthResponse response = AuthResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .specialization(saved.getSpecialization())
                .token(token)
                .message("Account created successfully")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Update Username ───────────────────────────────────────────────────────

    @PutMapping("/update-username")
    public ResponseEntity<AuthResponse> updateUsername(@Valid @RequestBody UpdateUsernameRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found with email: " + request.getEmail()));

        user.setFullName(request.getNewUsername().trim());
        User saved = userRepository.save(user);

        // If trainer, keep Trainer entity name synchronized
        if ("TRAINER".equalsIgnoreCase(saved.getRole())) {
            trainerRepository.findByEmailIgnoreCase(saved.getEmail()).ifPresent(trainer -> {
                trainer.setName(saved.getFullName());
                trainerRepository.save(trainer);
            });
        }

        String token = "FS-" + UUID.randomUUID().toString();

        AuthResponse response = AuthResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .specialization(saved.getSpecialization())
                .token(token)
                .message("Username successfully updated")
                .build();

        return ResponseEntity.ok(response);
    }

    // ─── Update Email ──────────────────────────────────────────────────────────

    @PutMapping("/update-email")
    public ResponseEntity<AuthResponse> updateEmail(@Valid @RequestBody UpdateEmailRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getCurrentEmail())
                .orElseThrow(() -> new BadRequestException("User not found with email: " + request.getCurrentEmail()));

        String newEmailLower = request.getNewEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(newEmailLower)) {
            throw new BadRequestException("An account with this email already exists.");
        }

        String oldEmail = user.getEmail();
        user.setEmail(newEmailLower);
        User saved = userRepository.save(user);

        // Keep Trainer entity email in sync if applicable
        if ("TRAINER".equalsIgnoreCase(saved.getRole())) {
            trainerRepository.findByEmailIgnoreCase(oldEmail).ifPresent(trainer -> {
                trainer.setEmail(newEmailLower);
                trainerRepository.save(trainer);
            });
        }

        String token = "FS-" + UUID.randomUUID().toString();

        AuthResponse response = AuthResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .specialization(saved.getSpecialization())
                .token(token)
                .message("Email successfully updated")
                .build();

        return ResponseEntity.ok(response);
    }

    // ─── Admin: Add Trainer ────────────────────────────────────────────────────

    @PostMapping("/admin/trainers")
    public ResponseEntity<Trainer> addTrainer(
            @RequestHeader("X-Admin-Email") String adminEmail,
            @Valid @RequestBody AddTrainerRequest request) {

        verifyAdmin(adminEmail);

        String emailLower = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(emailLower)) {
            throw new BadRequestException("An account with this email already exists.");
        }

        // Create trainer directory entry
        Trainer trainer = Trainer.builder()
                .name(request.getName().trim())
                .email(emailLower)
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 1)
                .bio(request.getBio() != null ? request.getBio() : "Certified personal trainer at FitSchedule.")
                .password(request.getPassword())
                .rating(4.8)
                .isVerified(true)
                .avatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl()
                        : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&fit=crop&crop=face")
                .build();
        trainerRepository.save(trainer);

        // Create corresponding user account so the trainer can log in
        User trainerUser = User.builder()
                .fullName(request.getName().trim())
                .email(emailLower)
                .password(request.getPassword())
                .role("TRAINER")
                .specialization(request.getSpecialization())
                .build();
        userRepository.save(trainerUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(trainer);
    }

    // ─── Admin: Remove Trainer ─────────────────────────────────────────────────

    @DeleteMapping("/admin/trainers/{trainerId}")
    public ResponseEntity<Void> removeTrainer(
            @RequestHeader("X-Admin-Email") String adminEmail,
            @PathVariable Long trainerId) {

        verifyAdmin(adminEmail);

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new BadRequestException("Trainer not found with ID: " + trainerId));

        // Also remove the trainer's user account if it exists
        userRepository.findByEmailIgnoreCase(trainer.getEmail())
                .ifPresent(userRepository::delete);

        trainerRepository.delete(trainer);
        return ResponseEntity.noContent().build();
    }

    // ─── Admin: List all trainers ──────────────────────────────────────────────

    @GetMapping("/admin/trainers")
    public ResponseEntity<List<Trainer>> listTrainersForAdmin(
            @RequestHeader("X-Admin-Email") String adminEmail) {

        verifyAdmin(adminEmail);
        return ResponseEntity.ok(trainerRepository.findAll());
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private void verifyAdmin(String adminEmail) {
        User admin = userRepository.findByEmailIgnoreCase(adminEmail)
                .orElseThrow(() -> new BadRequestException("Unauthorized: admin user not found."));
        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new BadRequestException("Unauthorized: only administrators can perform this action.");
        }
    }
}
