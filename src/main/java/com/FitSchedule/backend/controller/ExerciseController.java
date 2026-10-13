package com.FitSchedule.backend.controller;

import com.FitSchedule.backend.entity.Exercise;
import com.FitSchedule.backend.exception.ResourceNotFoundException;
import com.FitSchedule.backend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    @GetMapping
    public ResponseEntity<List<Exercise>> getExercises(
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String equipment) {

        if (muscleGroup != null && !muscleGroup.trim().isEmpty()) {
            return ResponseEntity.ok(exerciseRepository.findByMuscleGroupIgnoreCase(muscleGroup.trim()));
        }
        if (equipment != null && !equipment.trim().isEmpty()) {
            return ResponseEntity.ok(exerciseRepository.findByEquipmentIgnoreCase(equipment.trim()));
        }

        return ResponseEntity.ok(exerciseRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + id));
        return ResponseEntity.ok(exercise);
    }
}
