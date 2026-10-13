package com.FitSchedule.backend.service;

import com.FitSchedule.backend.dto.WorkoutDayDto;
import com.FitSchedule.backend.dto.WorkoutDayExerciseDto;
import com.FitSchedule.backend.dto.WorkoutPlanDto;
import com.FitSchedule.backend.entity.WorkoutDay;
import com.FitSchedule.backend.entity.WorkoutDayExercise;
import com.FitSchedule.backend.entity.WorkoutPlan;
import com.FitSchedule.backend.exception.BadRequestException;
import com.FitSchedule.backend.exception.ResourceNotFoundException;
import com.FitSchedule.backend.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    @Transactional(readOnly = true)
    public List<WorkoutPlanDto> getAllPlans(String userEmail) {
        List<WorkoutPlan> plans;
        if (userEmail != null && !userEmail.trim().isEmpty()) {
            plans = workoutPlanRepository.findByUserEmailIgnoreCaseOrderByCreatedAtDesc(userEmail.trim());
        } else {
            plans = workoutPlanRepository.findByUserEmailIsNullOrderByCreatedAtDesc();
        }
        return plans.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkoutPlanDto getPlanById(Long id) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + id));
        return mapToDto(plan);
    }

    @Transactional
    public WorkoutPlanDto createPlan(WorkoutPlanDto dto) {
        if (dto.getUserEmail() == null || dto.getUserEmail().trim().isEmpty()) {
            throw new BadRequestException("You must be logged in to create and save a workout plan.");
        }

        WorkoutPlan plan = new WorkoutPlan();
        updatePlanFieldsFromDto(plan, dto);

        if (dto.getDays() != null) {
            for (WorkoutDayDto dayDto : dto.getDays()) {
                WorkoutDay day = mapDayFromDto(dayDto);
                plan.addDay(day);
            }
        }

        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return mapToDto(saved);
    }

    @Transactional
    public WorkoutPlanDto updatePlan(Long id, WorkoutPlanDto dto) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with ID: " + id));

        updatePlanFieldsFromDto(plan, dto);

        // Clear existing days and add updated ones
        plan.getDays().clear();

        if (dto.getDays() != null) {
            for (WorkoutDayDto dayDto : dto.getDays()) {
                WorkoutDay day = mapDayFromDto(dayDto);
                plan.addDay(day);
            }
        }

        WorkoutPlan updated = workoutPlanRepository.save(plan);
        return mapToDto(updated);
    }

    @Transactional
    public void deletePlan(Long id) {
        if (!workoutPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout plan not found with ID: " + id);
        }
        workoutPlanRepository.deleteById(id);
    }

    private void updatePlanFieldsFromDto(WorkoutPlan plan, WorkoutPlanDto dto) {
        plan.setTitle(dto.getTitle());
        plan.setUserEmail(dto.getUserEmail());
        plan.setFitnessGoal(dto.getFitnessGoal());
        plan.setExperienceLevel(dto.getExperienceLevel());
        plan.setWorkoutFrequency(dto.getWorkoutFrequency());
        plan.setWorkoutDuration(dto.getWorkoutDuration());
        plan.setEquipment(dto.getEquipment());
        plan.setTrainingSplit(dto.getTrainingSplit());
        plan.setTargetMuscleGroups(dto.getTargetMuscleGroups());
        plan.setExercisesToAvoid(dto.getExercisesToAvoid());
        plan.setNotes(dto.getNotes());
    }

    public WorkoutPlanDto mapToDto(WorkoutPlan plan) {
        List<WorkoutDayDto> days = new ArrayList<>();
        if (plan.getDays() != null) {
            days = plan.getDays().stream()
                    .map(this::mapDayToDto)
                    .collect(Collectors.toList());
        }

        return WorkoutPlanDto.builder()
                .id(plan.getId())
                .title(plan.getTitle())
                .userEmail(plan.getUserEmail())
                .fitnessGoal(plan.getFitnessGoal())
                .experienceLevel(plan.getExperienceLevel())
                .workoutFrequency(plan.getWorkoutFrequency())
                .workoutDuration(plan.getWorkoutDuration())
                .equipment(plan.getEquipment())
                .trainingSplit(plan.getTrainingSplit())
                .targetMuscleGroups(plan.getTargetMuscleGroups())
                .exercisesToAvoid(plan.getExercisesToAvoid())
                .notes(plan.getNotes())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .days(days)
                .build();
    }

    private WorkoutDayDto mapDayToDto(WorkoutDay day) {
        List<WorkoutDayExerciseDto> exercises = new ArrayList<>();
        if (day.getExercises() != null) {
            exercises = day.getExercises().stream()
                    .map(ex -> WorkoutDayExerciseDto.builder()
                            .id(ex.getId())
                            .exerciseId(ex.getExerciseId())
                            .exerciseName(ex.getExerciseName())
                            .muscleGroup(ex.getMuscleGroup())
                            .subCategory(ex.getSubCategory())
                            .sets(ex.getSets())
                            .reps(ex.getReps())
                            .restSeconds(ex.getRestSeconds())
                            .orderIndex(ex.getOrderIndex())
                            .notes(ex.getNotes())
                            .build())
                    .collect(Collectors.toList());
        }

        return WorkoutDayDto.builder()
                .id(day.getId())
                .dayOfWeek(day.getDayOfWeek())
                .dayOrder(day.getDayOrder())
                .title(day.getTitle())
                .focus(day.getFocus())
                .estimatedDuration(day.getEstimatedDuration())
                .isRestDay(day.getIsRestDay() != null ? day.getIsRestDay() : false)
                .exercises(exercises)
                .build();
    }

    private WorkoutDay mapDayFromDto(WorkoutDayDto dayDto) {
        WorkoutDay day = WorkoutDay.builder()
                .dayOfWeek(dayDto.getDayOfWeek())
                .dayOrder(dayDto.getDayOrder() != null ? dayDto.getDayOrder() : 1)
                .title(dayDto.getTitle())
                .focus(dayDto.getFocus())
                .estimatedDuration(dayDto.getEstimatedDuration() != null ? dayDto.getEstimatedDuration() : 0)
                .isRestDay(dayDto.getIsRestDay() != null ? dayDto.getIsRestDay() : false)
                .exercises(new ArrayList<>())
                .build();

        if (dayDto.getExercises() != null) {
            for (int i = 0; i < dayDto.getExercises().size(); i++) {
                WorkoutDayExerciseDto exDto = dayDto.getExercises().get(i);
                WorkoutDayExercise exercise = WorkoutDayExercise.builder()
                        .exerciseId(exDto.getExerciseId())
                        .exerciseName(exDto.getExerciseName())
                        .muscleGroup(exDto.getMuscleGroup())
                        .subCategory(exDto.getSubCategory())
                        .sets(exDto.getSets() != null ? exDto.getSets() : 3)
                        .reps(exDto.getReps() != null ? exDto.getReps() : "10-12")
                        .restSeconds(exDto.getRestSeconds() != null ? exDto.getRestSeconds() : 60)
                        .orderIndex(exDto.getOrderIndex() != null ? exDto.getOrderIndex() : i + 1)
                        .notes(exDto.getNotes())
                        .build();
                day.addExercise(exercise);
            }
        }

        return day;
    }
}
