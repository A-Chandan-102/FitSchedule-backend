package com.FitSchedule.backend.service;

import com.FitSchedule.backend.dto.WorkoutDayDto;
import com.FitSchedule.backend.dto.WorkoutDayExerciseDto;
import com.FitSchedule.backend.dto.WorkoutGenerateRequest;
import com.FitSchedule.backend.dto.WorkoutPlanDto;
import com.FitSchedule.backend.entity.Exercise;
import com.FitSchedule.backend.exception.BadRequestException;
import com.FitSchedule.backend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutGeneratorService {

    private final ExerciseRepository exerciseRepository;

    private static final List<String> ALL_DAYS = Arrays.asList(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    );

    public WorkoutPlanDto generatePlan(WorkoutGenerateRequest request) {
        validateRequest(request);

        String title = request.getPlanTitle();
        if (title == null || title.trim().isEmpty()) {
            title = String.format("%s %s Plan (%d Days)",
                    request.getExperienceLevel(),
                    request.getFitnessGoal(),
                    request.getWorkoutFrequency());
        }

        String split = resolveTrainingSplit(request.getTrainingSplit(), request.getWorkoutFrequency());

        List<Exercise> allExercises = exerciseRepository.findAll();
        List<Exercise> eligibleExercises = filterExercisesByEquipmentAndAvoidance(
                allExercises,
                request.getEquipment(),
                request.getExercisesToAvoid()
        );

        // Fallback to all if filtering is too aggressive
        if (eligibleExercises.isEmpty()) {
            eligibleExercises = allExercises;
        }

        int targetExerciseCount = calculateExerciseCount(request.getWorkoutDuration());
        RepSetRule repSetRule = determineRepSetRule(request.getFitnessGoal(), request.getExperienceLevel());

        // Sort selected days by standard weekday order
        List<String> selectedDays = request.getPreferredDays().stream()
                .filter(ALL_DAYS::contains)
                .sorted(Comparator.comparingInt(ALL_DAYS::indexOf))
                .collect(Collectors.toList());

        List<WorkoutDayDto> generatedDays = new ArrayList<>();
        int trainingDayIndex = 0;

        for (int i = 0; i < ALL_DAYS.size(); i++) {
            String dayName = ALL_DAYS.get(i);
            boolean isTraining = selectedDays.contains(dayName);

            if (isTraining) {
                DayFocusConfig focusConfig = getDayFocus(split, trainingDayIndex, request.getWorkoutFrequency());
                trainingDayIndex++;

                List<WorkoutDayExerciseDto> dayExercises = pickExercisesForFocus(
                        eligibleExercises,
                        focusConfig.muscleGroups,
                        targetExerciseCount,
                        repSetRule,
                        request.getTargetMuscleGroups()
                );

                // For Fat Loss goal, append dedicated time-based cardio finisher
                if (request.getFitnessGoal() != null && request.getFitnessGoal().equalsIgnoreCase("Fat loss")) {
                    List<Exercise> cardioPool = allExercises.stream()
                            .filter(e -> "Cardio".equalsIgnoreCase(e.getMuscleGroup()))
                            .collect(Collectors.toList());
                    if (!cardioPool.isEmpty()) {
                        Exercise cardioEx = cardioPool.get((trainingDayIndex - 1) % cardioPool.size());
                        dayExercises.add(WorkoutDayExerciseDto.builder()
                                .exerciseId(cardioEx.getId())
                                .exerciseName(cardioEx.getName())
                                .muscleGroup(cardioEx.getMuscleGroup())
                                .subCategory(cardioEx.getSubCategory())
                                .sets(1)
                                .reps(cardioEx.getDefaultReps() != null ? cardioEx.getDefaultReps() : "25-30 mins")
                                .restSeconds(60)
                                .orderIndex(dayExercises.size() + 1)
                                .notes(cardioEx.getDescription())
                                .build());
                    }
                }

                WorkoutDayDto dayDto = WorkoutDayDto.builder()
                        .dayOfWeek(dayName)
                        .dayOrder(i + 1)
                        .title(focusConfig.title)
                        .focus(focusConfig.focusSummary)
                        .estimatedDuration(request.getWorkoutDuration())
                        .isRestDay(false)
                        .exercises(dayExercises)
                        .build();

                generatedDays.add(dayDto);
            } else {
                // Rest day
                WorkoutDayDto restDayDto = WorkoutDayDto.builder()
                        .dayOfWeek(dayName)
                        .dayOrder(i + 1)
                        .title("Rest & Active Recovery")
                        .focus("Hydration, light stretching, mobility, and muscle restoration")
                        .estimatedDuration(0)
                        .isRestDay(true)
                        .exercises(new ArrayList<>())
                        .build();

                generatedDays.add(restDayDto);
            }
        }

        return WorkoutPlanDto.builder()
                .title(title)
                .fitnessGoal(request.getFitnessGoal())
                .experienceLevel(request.getExperienceLevel())
                .workoutFrequency(request.getWorkoutFrequency())
                .workoutDuration(request.getWorkoutDuration())
                .equipment(request.getEquipment())
                .trainingSplit(split)
                .targetMuscleGroups(request.getTargetMuscleGroups() != null ? request.getTargetMuscleGroups() : "")
                .exercisesToAvoid(request.getExercisesToAvoid() != null ? request.getExercisesToAvoid() : "")
                .notes(request.getNotes() != null ? request.getNotes() : "Generated personalized plan based on user goals and equipment.")
                .days(generatedDays)
                .build();
    }

    private void validateRequest(WorkoutGenerateRequest request) {
        if (request.getPreferredDays() == null || request.getPreferredDays().isEmpty()) {
            throw new BadRequestException("Please select preferred training days.");
        }
        if (request.getPreferredDays().size() != request.getWorkoutFrequency()) {
            throw new BadRequestException(String.format(
                    "You chose a frequency of %d days per week, but selected %d days. The numbers must match.",
                    request.getWorkoutFrequency(), request.getPreferredDays().size()
            ));
        }
    }

    private String resolveTrainingSplit(String requestedSplit, int frequency) {
        if (requestedSplit != null && !requestedSplit.equalsIgnoreCase("Auto")
                && !requestedSplit.toLowerCase().contains("automatic")) {
            return requestedSplit;
        }

        if (frequency <= 3) {
            return "Full body";
        } else if (frequency == 4) {
            return "Upper/lower";
        } else {
            return "Push/pull/legs";
        }
    }

    private List<Exercise> filterExercisesByEquipmentAndAvoidance(
            List<Exercise> exercises, String userEquipment, String avoidText) {

        List<String> avoidTerms = new ArrayList<>();
        if (avoidText != null && !avoidText.trim().isEmpty()) {
            avoidTerms = Arrays.stream(avoidText.split(","))
                    .map(term -> term.trim().toLowerCase())
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        List<String> finalAvoidTerms = avoidTerms;

        return exercises.stream()
                .filter(e -> {
                    // Equipment match
                    if (userEquipment.equalsIgnoreCase("Full gym")) {
                        return true;
                    } else if (userEquipment.equalsIgnoreCase("Dumbbells")) {
                        return e.getEquipment().equalsIgnoreCase("Dumbbells")
                                || e.getEquipment().equalsIgnoreCase("Bodyweight only");
                    } else if (userEquipment.equalsIgnoreCase("Resistance bands")) {
                        return e.getEquipment().equalsIgnoreCase("Resistance bands")
                                || e.getEquipment().equalsIgnoreCase("Bodyweight only");
                    } else if (userEquipment.equalsIgnoreCase("Bodyweight only")) {
                        return e.getEquipment().equalsIgnoreCase("Bodyweight only");
                    } else { // Limited or custom equipment
                        return !e.getEquipment().equalsIgnoreCase("Full gym");
                    }
                })
                .filter(e -> {
                    if (finalAvoidTerms.isEmpty()) return true;
                    String exerciseNameLower = e.getName().toLowerCase();
                    return finalAvoidTerms.stream().noneMatch(exerciseNameLower::contains);
                })
                .collect(Collectors.toList());
    }

    private int calculateExerciseCount(int duration) {
        if (duration <= 30) return 4;
        if (duration <= 45) return 5;
        if (duration <= 60) return 6;
        return 7; // 90 mins
    }

    private RepSetRule determineRepSetRule(String goal, String level) {
        String reps;
        int sets;
        int rest;

        if (goal.equalsIgnoreCase("Strength development")) {
            sets = level.equalsIgnoreCase("Beginner") ? 3 : 4;
            reps = "5-6 reps";
            rest = 90;
        } else if (goal.equalsIgnoreCase("Muscle gain")) {
            sets = level.equalsIgnoreCase("Advanced") ? 4 : 3;
            reps = "8-12 reps";
            rest = 75;
        } else if (goal.equalsIgnoreCase("Fat loss") || goal.equalsIgnoreCase("General fitness")) {
            sets = 3;
            reps = "12-15 reps";
            rest = 60;
        } else { // Endurance
            sets = 3;
            reps = "15-20 reps";
            rest = 45;
        }

        return new RepSetRule(sets, reps, rest);
    }

    private DayFocusConfig getDayFocus(String split, int trainingDayIndex, int frequency) {
        if (split.equalsIgnoreCase("Full body")) {
            int variation = trainingDayIndex % 3;
            if (variation == 0) {
                return new DayFocusConfig(
                        "Full Body A - Chest & Quad Focus",
                        "Chest, Quads, Back & Core",
                        Arrays.asList("Chest", "Legs", "Back", "Core")
                );
            } else if (variation == 1) {
                return new DayFocusConfig(
                        "Full Body B - Posterior Chain & Shoulders",
                        "Back, Hamstrings/Glutes, Shoulders & Arms",
                        Arrays.asList("Back", "Legs", "Shoulders", "Arms")
                );
            } else {
                return new DayFocusConfig(
                        "Full Body C - Hypertrophy & Conditioning",
                        "Total body compound supersets & Core endurance",
                        Arrays.asList("Legs", "Chest", "Back", "Shoulders", "Core")
                );
            }
        } else if (split.equalsIgnoreCase("Upper/lower")) {
            int variation = trainingDayIndex % 4;
            if (variation == 0) {
                return new DayFocusConfig(
                        "Upper Body Power",
                        "Chest, Upper Back, Shoulders & Arms",
                        Arrays.asList("Chest", "Back", "Shoulders", "Arms")
                );
            } else if (variation == 1) {
                return new DayFocusConfig(
                        "Lower Body & Core Strength",
                        "Quads, Hamstrings, Glutes & Abs",
                        Arrays.asList("Legs", "Core")
                );
            } else if (variation == 2) {
                return new DayFocusConfig(
                        "Upper Body Hypertrophy",
                        "Chest, Back width, Deltoids & Arms",
                        Arrays.asList("Chest", "Back", "Shoulders", "Arms")
                );
            } else {
                return new DayFocusConfig(
                        "Lower Body & Core Volume",
                        "Glutes, Hamstring curls, Calves & Core",
                        Arrays.asList("Legs", "Core")
                );
            }
        } else { // Push/pull/legs
            int variation = trainingDayIndex % 3;
            if (variation == 0) {
                return new DayFocusConfig(
                        "Push Day - Chest, Shoulders & Triceps",
                        "Pectorals, Anterior Delts, Lateral Delts & Triceps",
                        Arrays.asList("Chest", "Shoulders", "Arms")
                );
            } else if (variation == 1) {
                return new DayFocusConfig(
                        "Pull Day - Back & Biceps",
                        "Lats, Traps, Rhomboids, Rear Delts & Biceps",
                        Arrays.asList("Back", "Arms", "Shoulders")
                );
            } else {
                return new DayFocusConfig(
                        "Legs & Core Day",
                        "Quadriceps, Hamstrings, Glutes, Calves & Abs",
                        Arrays.asList("Legs", "Core")
                );
            }
        }
    }

    private List<WorkoutDayExerciseDto> pickExercisesForFocus(
            List<Exercise> pool,
            List<String> targetMuscles,
            int count,
            RepSetRule repSetRule,
            String userPriorityMuscles) {

        List<WorkoutDayExerciseDto> result = new ArrayList<>();
        Set<String> addedNames = new HashSet<>();

        // Group exercises by muscle
        Map<String, List<Exercise>> byMuscle = pool.stream()
                .collect(Collectors.groupingBy(e -> e.getMuscleGroup().toLowerCase()));

        // Cycle through target muscles to ensure balanced distribution
        int order = 1;
        int muscleIdx = 0;

        while (result.size() < count && muscleIdx < targetMuscles.size() * 3) {
            String muscle = targetMuscles.get(muscleIdx % targetMuscles.size()).toLowerCase();
            List<Exercise> candidates = byMuscle.getOrDefault(muscle, Collections.emptyList());

            for (Exercise ex : candidates) {
                if (!addedNames.contains(ex.getName())) {
                    addedNames.add(ex.getName());
                    result.add(WorkoutDayExerciseDto.builder()
                            .exerciseId(ex.getId())
                            .exerciseName(ex.getName())
                            .muscleGroup(ex.getMuscleGroup())
                            .subCategory(ex.getSubCategory())
                            .sets(repSetRule.sets)
                            .reps(repSetRule.reps)
                            .restSeconds(repSetRule.restSeconds)
                            .orderIndex(order++)
                            .notes(ex.getDescription())
                            .build());
                    break;
                }
            }
            muscleIdx++;
        }

        // If count still not reached, fill with any unused exercises from pool
        for (Exercise ex : pool) {
            if (result.size() >= count) break;
            if (!addedNames.contains(ex.getName())) {
                addedNames.add(ex.getName());
                result.add(WorkoutDayExerciseDto.builder()
                        .exerciseId(ex.getId())
                        .exerciseName(ex.getName())
                        .muscleGroup(ex.getMuscleGroup())
                        .subCategory(ex.getSubCategory())
                        .sets(repSetRule.sets)
                        .reps(repSetRule.reps)
                        .restSeconds(repSetRule.restSeconds)
                        .orderIndex(order++)
                        .notes(ex.getDescription())
                        .build());
            }
        }

        return result;
    }

    private static class RepSetRule {
        final int sets;
        final String reps;
        final int restSeconds;

        RepSetRule(int sets, String reps, int restSeconds) {
            this.sets = sets;
            this.reps = reps;
            this.restSeconds = restSeconds;
        }
    }

    private static class DayFocusConfig {
        final String title;
        final String focusSummary;
        final List<String> muscleGroups;

        DayFocusConfig(String title, String focusSummary, List<String> muscleGroups) {
            this.title = title;
            this.focusSummary = focusSummary;
            this.muscleGroups = muscleGroups;
        }
    }
}
