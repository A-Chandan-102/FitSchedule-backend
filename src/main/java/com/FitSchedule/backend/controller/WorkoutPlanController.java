package com.FitSchedule.backend.controller;

import com.FitSchedule.backend.dto.WorkoutGenerateRequest;
import com.FitSchedule.backend.dto.WorkoutPlanDto;
import com.FitSchedule.backend.service.WorkoutGeneratorService;
import com.FitSchedule.backend.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;
    private final WorkoutGeneratorService workoutGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<WorkoutPlanDto> generatePlan(@Valid @RequestBody WorkoutGenerateRequest request) {
        WorkoutPlanDto generated = workoutGeneratorService.generatePlan(request);
        return ResponseEntity.ok(generated);
    }

    @PostMapping
    public ResponseEntity<WorkoutPlanDto> createPlan(@Valid @RequestBody WorkoutPlanDto dto) {
        WorkoutPlanDto created = workoutPlanService.createPlan(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutPlanDto>> getAllPlans(@RequestParam(required = false) String userEmail) {
        List<WorkoutPlanDto> plans = workoutPlanService.getAllPlans(userEmail);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanDto> getPlanById(@PathVariable Long id) {
        WorkoutPlanDto plan = workoutPlanService.getPlanById(id);
        return ResponseEntity.ok(plan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanDto> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutPlanDto dto) {
        WorkoutPlanDto updated = workoutPlanService.updatePlan(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePlan(@PathVariable Long id) {
        workoutPlanService.deletePlan(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Workout plan deleted successfully.");
        response.put("id", id);
        return ResponseEntity.ok(response);
    }
}
