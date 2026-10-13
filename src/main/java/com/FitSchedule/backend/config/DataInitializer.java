package com.FitSchedule.backend.config;

import com.FitSchedule.backend.entity.Exercise;
import com.FitSchedule.backend.entity.Trainer;
import com.FitSchedule.backend.entity.User;
import com.FitSchedule.backend.repository.ExerciseRepository;
import com.FitSchedule.backend.repository.TrainerRepository;
import com.FitSchedule.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    public DataInitializer(ExerciseRepository exerciseRepository,
                           TrainerRepository trainerRepository,
                           UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        seedTrainersAndUsers();
        seedExercises();
    }

    private void seedExercises() {
        // Re-seed if database has outdated schema or fewer exercises
        if (exerciseRepository.count() >= 75 && exerciseRepository.findAll().stream().allMatch(e -> e.getSubCategory() != null)) {
            return;
        }

        exerciseRepository.deleteAll();

        List<Exercise> exercises = Arrays.asList(
                // ==================== 1. CHEST ====================
                // Upper Chest
                Exercise.builder()
                        .name("Incline Barbell Bench Press")
                        .muscleGroup("Chest").subCategory("Upper chest").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Set bench to 30-45 degrees, lower bar to upper clavicle, drive upward.")
                        .defaultSets(4).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Incline Dumbbell Press")
                        .muscleGroup("Chest").subCategory("Upper chest").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Incline bench at 30 degrees, press dumbbells upward focusing on upper chest contraction.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Incline Dumbbell Fly")
                        .muscleGroup("Chest").subCategory("Upper chest").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Open arms in wide arc on incline bench, feeling deep upper pec stretch.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Low-to-High Cable Crossover")
                        .muscleGroup("Chest").subCategory("Upper chest").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Pull cable handles upward and together, emphasizing upper pectoral clavicular head.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Incline Smith Machine Press")
                        .muscleGroup("Chest").subCategory("Upper chest").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Controlled fixed-path incline press targeting upper pecs with maximum stability.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),

                // Lower Chest
                Exercise.builder()
                        .name("Decline Barbell Bench Press")
                        .muscleGroup("Chest").subCategory("Lower chest").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Lie on decline bench, lower barbell smoothly to lower pectorals and press.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Decline Dumbbell Bench Press")
                        .muscleGroup("Chest").subCategory("Lower chest").category("Compound").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Press dumbbells on decline bench, targeting lower pectoral ridge.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("High-to-Low Cable Crossover")
                        .muscleGroup("Chest").subCategory("Lower chest").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Pull cables downward across hips to strongly contract lower chest fibers.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Chest Dips")
                        .muscleGroup("Chest").subCategory("Lower chest").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Lean torso forward 30 degrees on dip bars, lower until pecs stretch, press back up.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),

                // Mid Chest
                Exercise.builder()
                        .name("Barbell Bench Press")
                        .muscleGroup("Chest").subCategory("Mid chest").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Grip slightly wider than shoulder width, lower bar to sternum, drive through chest.")
                        .defaultSets(4).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Flat Dumbbell Bench Press")
                        .muscleGroup("Chest").subCategory("Mid chest").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Independent unilateral flat dumbbell press allowing deep natural stretch at bottom.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Flat Dumbbell Fly")
                        .muscleGroup("Chest").subCategory("Mid chest").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Maintain slight elbow bend, hug inward over chest, focusing on mid pec squeeze.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Pec Deck Machine Fly")
                        .muscleGroup("Chest").subCategory("Mid chest").category("Isolation").equipment("Full gym")
                        .experienceLevel("Beginner").description("Keep elbows at shoulder level, contract handles together, pause at peak squeeze.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Standing Cable Chest Press")
                        .muscleGroup("Chest").subCategory("Mid chest").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Anchor cables at mid-chest height, press forward with steady continuous tension.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),

                // Full Chest
                Exercise.builder()
                        .name("Standard Push-Ups")
                        .muscleGroup("Chest").subCategory("Full chest").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("All").description("Plank alignment, lower chest near floor, press up firmly engaging whole pectoral wall.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Wide-Grip Push-Ups")
                        .muscleGroup("Chest").subCategory("Full chest").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Hands set 1.5x shoulder width apart to maximize pectoral stretch.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Banded Chest Press")
                        .muscleGroup("Chest").subCategory("Full chest").category("Compound").equipment("Resistance bands")
                        .experienceLevel("Beginner").description("Wrap resistance band behind back, push handles forward to full lockout.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Diamond Push-Ups")
                        .muscleGroup("Chest").subCategory("Full chest").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Hands close with index and thumb touching in diamond shape under center chest.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // ==================== 2. BACK ====================
                // Upper Back
                Exercise.builder()
                        .name("Barbell Bent-Over Row")
                        .muscleGroup("Back").subCategory("Upper back").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Hinge forward 45 degrees, pull barbell toward sternum pinching upper shoulder blades.")
                        .defaultSets(4).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("T-Bar Row")
                        .muscleGroup("Back").subCategory("Upper back").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Straddle bar, pull handle to chest, squeezing rhomboids and middle trapezius.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Seated Cable Face Pull")
                        .muscleGroup("Back").subCategory("Upper back").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Pull rope attachment toward forehead, externally rotating shoulders.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Dumbbell Shrugs")
                        .muscleGroup("Back").subCategory("Upper back").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Elevate shoulders straight toward ears, hold for 1 second, lower under control.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Incline Prone Dumbbell Row")
                        .muscleGroup("Back").subCategory("Upper back").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Chest-supported on incline bench, row dumbbells with elbows flared for upper back focus.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // Mid Back
                Exercise.builder()
                        .name("Seated Cable Row")
                        .muscleGroup("Back").subCategory("Mid back").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Sit upright, pull V-handle to abdomen while retracting scapulae smoothly.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Single-Arm Dumbbell Row")
                        .muscleGroup("Back").subCategory("Mid back").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("One knee on bench, pull dumbbell to hip level keeping back flat and core tight.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Chest-Supported Machine Row")
                        .muscleGroup("Back").subCategory("Mid back").category("Compound").equipment("Full gym")
                        .experienceLevel("Beginner").description("Drive elbows backward against chest pad without lumbar spinal stress.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Inverted Bodyweight Row")
                        .muscleGroup("Back").subCategory("Mid back").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("All").description("Hang under barbell or Smith machine bar, pull chest up toward the bar.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // Lower Back
                Exercise.builder()
                        .name("Barbell Deadlift")
                        .muscleGroup("Back").subCategory("Lower back").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Hinge at hips, grip barbell, drive through feet extending hips and spine together.")
                        .defaultSets(4).defaultReps("5-6").defaultRestSeconds(120).build(),
                Exercise.builder()
                        .name("Romanian Deadlift")
                        .muscleGroup("Back").subCategory("Lower back").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Slight knee bend, hinge hips backward until hamstrings and erector spinae stretch.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Hyperextensions / Back Extensions")
                        .muscleGroup("Back").subCategory("Lower back").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("All").description("Anchor thighs on 45-degree bench, lower torso and extend using erectors and glutes.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Good Mornings")
                        .muscleGroup("Back").subCategory("Lower back").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Barbell on traps, hinge at hips with flat back, return upright using posterior chain.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Superman Hold")
                        .muscleGroup("Back").subCategory("Lower back").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Lie on stomach, elevate arms, chest, and legs simultaneously, hold steady.")
                        .defaultSets(3).defaultReps("30-45 sec").defaultRestSeconds(45).build(),

                // Lats
                Exercise.builder()
                        .name("Wide-Grip Pull-Ups")
                        .muscleGroup("Back").subCategory("Lats").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Overhand wide grip, pull body upward until chin clears bar, lower under control.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Lat Pulldown")
                        .muscleGroup("Back").subCategory("Lats").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Wide grip bar, pull down toward collarbone squeezing lats, return smoothly.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Close-Grip Cable Pulldown")
                        .muscleGroup("Back").subCategory("Lats").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Use V-bar handle, pull down into sternum driving elbows down toward hips.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Straight-Arm Cable Pulldown")
                        .muscleGroup("Back").subCategory("Lats").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Arms extended with slight elbow bend, sweep bar in arc down to thighs.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Underhand Chin-Ups")
                        .muscleGroup("Back").subCategory("Lats").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Supinated grip, pull up to bar engaging both lower lats and biceps.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(90).build(),

                // Full Back
                Exercise.builder()
                        .name("Rack Pulls")
                        .muscleGroup("Back").subCategory("Full back").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Partial deadlift from knee level inside rack, overloading entire back musculature.")
                        .defaultSets(3).defaultReps("6-8").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Resistance Band Lat Row")
                        .muscleGroup("Back").subCategory("Full back").category("Compound").equipment("Resistance bands")
                        .experienceLevel("Beginner").description("Anchor band at chest height, pull elbows back while engaging shoulder blades.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Pendlay Row")
                        .muscleGroup("Back").subCategory("Full back").category("Compound").equipment("Full gym")
                        .experienceLevel("Advanced").description("Explosive horizontal row starting dead off the floor on each repetition.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(90).build(),

                // ==================== 3. LEGS ====================
                // Quads
                Exercise.builder()
                        .name("Barbell Back Squat")
                        .muscleGroup("Legs").subCategory("Quads").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Barbell on upper back, descend until thighs parallel floor, drive through mid-foot.")
                        .defaultSets(4).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Barbell Front Squat")
                        .muscleGroup("Legs").subCategory("Quads").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Bar on front clavicle rack, deep vertical squat heavily targeting quads.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Leg Press")
                        .muscleGroup("Legs").subCategory("Quads").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Feet shoulder-width on platform, lower sled with controlled knees, press back up.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Leg Extension Machine")
                        .muscleGroup("Legs").subCategory("Quads").category("Isolation").equipment("Full gym")
                        .experienceLevel("Beginner").description("Extend knees fully, hold peak quad contraction for 1 second, lower smoothly.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Dumbbell Goblet Squat")
                        .muscleGroup("Legs").subCategory("Quads").category("Compound").equipment("Dumbbells")
                        .experienceLevel("Beginner").description("Hold single dumbbell at chest, maintain upright posture, squat to parallel.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Bulgarian Split Squat")
                        .muscleGroup("Legs").subCategory("Quads").category("Compound").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Rear foot elevated on bench, drop back knee down into deep single-leg quad burn.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Bodyweight Air Squats")
                        .muscleGroup("Legs").subCategory("Quads").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Smooth rhythmic bodyweight squats to parallel for conditioning and mobility.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),

                // Glutes
                Exercise.builder()
                        .name("Barbell Hip Thrust")
                        .muscleGroup("Legs").subCategory("Glutes").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Upper back on bench, drive hips upward to ceiling squeezing glutes at top.")
                        .defaultSets(4).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Dumbbell Glute Bridge")
                        .muscleGroup("Legs").subCategory("Glutes").category("Compound").equipment("Dumbbells")
                        .experienceLevel("Beginner").description("Lie on floor with dumbbell across hips, drive heels into ground and lift pelvis.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cable Pull-Through")
                        .muscleGroup("Legs").subCategory("Glutes").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Stand facing away from low pulley with rope between legs, hinge hips back and snap forward.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cable Glute Kickbacks")
                        .muscleGroup("Legs").subCategory("Glutes").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Ankle cuff on low cable, kick leg backward squeezing glute maximus firmly.")
                        .defaultSets(3).defaultReps("15").defaultRestSeconds(45).build(),

                // Hamstrings
                Exercise.builder()
                        .name("Dumbbell Romanian Deadlift")
                        .muscleGroup("Legs").subCategory("Hamstrings").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Hold dumbbells close to shins, hinge hips back until deep stretch in hamstring bellies.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Lying Leg Curl Machine")
                        .muscleGroup("Legs").subCategory("Hamstrings").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Lie prone, curl roller toward glutes, pause, lower slowly against resistance.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Seated Leg Curl Machine")
                        .muscleGroup("Legs").subCategory("Hamstrings").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Curl heels under seat while hips remain flexed, maximizing hamstring leverage.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Single-Leg Dumbbell RDL")
                        .muscleGroup("Legs").subCategory("Hamstrings").category("Compound").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Balance on one leg while extending opposing leg back, hinging forward with dumbbell.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // Calves
                Exercise.builder()
                        .name("Standing Calf Raise")
                        .muscleGroup("Legs").subCategory("Calves").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Balls of feet on platform, drop heels low for full stretch, push high on toes.")
                        .defaultSets(4).defaultReps("12-15").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Seated Calf Raise")
                        .muscleGroup("Legs").subCategory("Calves").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Knees bent 90 degrees under pad, targeting the deeper soleus calf muscle.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Single-Leg Bodyweight Calf Raise")
                        .muscleGroup("Legs").subCategory("Calves").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Stand on edge of step, rise onto ball of single foot, pause at apex.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(30).build(),

                // Full Legs
                Exercise.builder()
                        .name("Trap Bar Deadlift")
                        .muscleGroup("Legs").subCategory("Full legs").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Stand inside hex bar, neutral hand grip, drive through quads and glutes to stand.")
                        .defaultSets(4).defaultReps("6-8").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Walking Lunges")
                        .muscleGroup("Legs").subCategory("Full legs").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Step forward in alternating lunges, tracking front knee over midfoot.")
                        .defaultSets(3).defaultReps("12 reps/leg").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Wall Sit Isometric Hold")
                        .muscleGroup("Legs").subCategory("Full legs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Back flat against wall, knees bent 90 degrees, hold position statically.")
                        .defaultSets(3).defaultReps("45-60 sec").defaultRestSeconds(45).build(),

                // ==================== 4. ARMS ====================
                // Long Head Bicep
                Exercise.builder()
                        .name("Incline Dumbbell Bicep Curl")
                        .muscleGroup("Arms").subCategory("Long head bicep").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Bench at 45 degrees, stretch long head of bicep behind torso before curling upward.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Close-Grip EZ-Bar Curl")
                        .muscleGroup("Arms").subCategory("Long head bicep").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Hands placed inside inner curves of EZ-bar to shift emphasis toward outer long head.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Behind-the-Back Cable Curl")
                        .muscleGroup("Arms").subCategory("Long head bicep").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Step forward from low pulley, arm stretched back, curl forward with deep stretch.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),

                // Short Head Bicep
                Exercise.builder()
                        .name("Preacher Curl")
                        .muscleGroup("Arms").subCategory("Short head Bicep").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Arms rested on inclined preacher pad, isolating inner short head and peak.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Dumbbell Concentration Curl")
                        .muscleGroup("Arms").subCategory("Short head Bicep").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Elbow braced against inner thigh, curl dumbbell with strict non-momentum form.")
                        .defaultSets(3).defaultReps("12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Wide-Grip Barbell Curl")
                        .muscleGroup("Arms").subCategory("Short head Bicep").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Hands placed wider than shoulders on barbell to recruit inner bicep head.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Spider Curl")
                        .muscleGroup("Arms").subCategory("Short head Bicep").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Chest on vertical face of preacher bench, hang arms and curl to eliminate shoulder swing.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // Full Biceps
                Exercise.builder()
                        .name("Standing Alternating Dumbbell Curl")
                        .muscleGroup("Arms").subCategory("Full biceps").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Supinate wrists as dumbbells rise, fully activating overall bicep belly.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Standing Barbell Curl")
                        .muscleGroup("Arms").subCategory("Full biceps").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Classic strict straight-bar curl for complete bicep mass development.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Resistance Band Bicep Curl")
                        .muscleGroup("Arms").subCategory("Full biceps").category("Isolation").equipment("Resistance bands")
                        .experienceLevel("Beginner").description("Stand on loop band, curl handles upward with progressive elastic tension.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),

                // Triceps
                Exercise.builder()
                        .name("Triceps Cable Pushdown")
                        .muscleGroup("Arms").subCategory("Triceps").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Keep elbows pinned to ribs, press bar down and lock triceps at bottom.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Skull Crushers (Lying Triceps Extension)")
                        .muscleGroup("Arms").subCategory("Triceps").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Lie on bench with EZ bar, bend at elbows lowering bar toward forehead.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Overhead Dumbbell Triceps Extension")
                        .muscleGroup("Arms").subCategory("Triceps").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Lower dumbbell behind head, extend elbows upward stretching long triceps head.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Close-Grip Barbell Bench Press")
                        .muscleGroup("Arms").subCategory("Triceps").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Grip hands shoulder-width on barbell, tuck elbows and press with triceps.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Bench Dips")
                        .muscleGroup("Arms").subCategory("Triceps").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Hands on edge of bench behind back, lower hips and press upward using triceps.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cable Overhead Rope Extension")
                        .muscleGroup("Arms").subCategory("Triceps").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Face away from high cable pulley, extend rope overhead spreading ends outward.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),

                // Brachialis
                Exercise.builder()
                        .name("Standing Dumbbell Hammer Curl")
                        .muscleGroup("Arms").subCategory("brachialis").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Neutral grip with palms facing each other, curls targeting brachialis and arm thickness.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cable Rope Hammer Curl")
                        .muscleGroup("Arms").subCategory("brachialis").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Neutral rope grip from low cable, constant tension on brachialis muscle belly.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cross-Body Dumbbell Hammer Curl")
                        .muscleGroup("Arms").subCategory("brachialis").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Curl dumbbell diagonally across opposite chest, engaging brachialis deeply.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Reverse Grip Barbell Curl")
                        .muscleGroup("Arms").subCategory("brachialis").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Overhand grip on barbell, curls building brachialis and brachioradialis.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // Forearms
                Exercise.builder()
                        .name("Palms-Up Barbell Wrist Curl")
                        .muscleGroup("Arms").subCategory("forearms").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Forearms flat on bench, curl wrists upward contracting forearm flexors.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Palms-Down Wrist Curl")
                        .muscleGroup("Arms").subCategory("forearms").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Forearms on knees, extend wrists upward contracting forearm extensors.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Farmer's Carry Walk")
                        .muscleGroup("Arms").subCategory("forearms").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Carry heavy dumbbells at sides, walking tall with crushing grip engagement.")
                        .defaultSets(3).defaultReps("45-60 sec").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Dead Hang from Pull-Up Bar")
                        .muscleGroup("Arms").subCategory("forearms").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("All").description("Hang relaxed from overhead bar for grip strength and shoulder decompression.")
                        .defaultSets(3).defaultReps("45-60 sec").defaultRestSeconds(60).build(),

                // ==================== 5. CORE ====================
                // Upper Abs
                Exercise.builder()
                        .name("Standard Floor Crunches")
                        .muscleGroup("Core").subCategory("Upper abs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Curl ribcage down toward pelvis, focusing on contracting upper rectus abdominis.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Cable Kneeling Crunch")
                        .muscleGroup("Core").subCategory("Upper abs").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Kneel facing cable stack, crunch spine downward drawing elbows toward knees.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Decline Bench Crunch")
                        .muscleGroup("Core").subCategory("Upper abs").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Decline bench crunch for increased range of motion and core resistance.")
                        .defaultSets(3).defaultReps("15").defaultRestSeconds(45).build(),

                // Lower Abs
                Exercise.builder()
                        .name("Hanging Leg Raise")
                        .muscleGroup("Core").subCategory("lower abs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Hang from bar, curl pelvis upward bringing straight legs to 90 degrees.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Captain's Chair Knee Raise")
                        .muscleGroup("Core").subCategory("lower abs").category("Isolation").equipment("Full gym")
                        .experienceLevel("Beginner").description("Forearms supported on parallel pads, raise knees smoothly toward chest.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Reverse Crunches")
                        .muscleGroup("Core").subCategory("lower abs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Lie on back, lift hips and knees off floor toward ribcage.")
                        .defaultSets(3).defaultReps("15-20").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Lying Flutter Kicks")
                        .muscleGroup("Core").subCategory("lower abs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Beginner").description("Low back pressed to floor, flutter feet alternating in small rapid arcs.")
                        .defaultSets(3).defaultReps("30-45 sec").defaultRestSeconds(45).build(),

                // Obliques
                Exercise.builder()
                        .name("Russian Twists")
                        .muscleGroup("Core").subCategory("Obliques").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Sit at 45-degree angle, rotate weight across hips engaging lateral obliques.")
                        .defaultSets(3).defaultReps("20 total").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Bicycle Crunches")
                        .muscleGroup("Core").subCategory("Obliques").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("All").description("Cross elbow to opposite knee while extending opposing leg in cycling motion.")
                        .defaultSets(3).defaultReps("20 total").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Side Plank Hold")
                        .muscleGroup("Core").subCategory("Obliques").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("All").description("Forearm on floor, stack feet, keep body in rigid diagonal line holding obliques tight.")
                        .defaultSets(3).defaultReps("30-45 sec/side").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Cable Woodchopper")
                        .muscleGroup("Core").subCategory("Obliques").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Pull cable diagonally across torso in rotational power pattern.")
                        .defaultSets(3).defaultReps("12 reps/side").defaultRestSeconds(45).build(),

                // Full Abs
                Exercise.builder()
                        .name("Standard Elbow Plank")
                        .muscleGroup("Core").subCategory("full abs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("All").description("Maintain rigid straight plank, engaging transverse abdominis and glutes.")
                        .defaultSets(3).defaultReps("45-60 sec").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Ab Wheel Rollout")
                        .muscleGroup("Core").subCategory("full abs").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Advanced").description("Kneel and roll wheel forward keeping core braced, pull back with abs.")
                        .defaultSets(3).defaultReps("8-10").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Mountain Climbers")
                        .muscleGroup("Core").subCategory("full abs").category("Cardio/Core").equipment("Bodyweight only")
                        .experienceLevel("All").description("Push-up position, alternate driving knees rapidly toward chest.")
                        .defaultSets(3).defaultReps("30-40 sec").defaultRestSeconds(45).build(),
                Exercise.builder()
                        .name("Hollow Body Hold")
                        .muscleGroup("Core").subCategory("full abs").category("Isolation").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Banana posture with lower back pressed firmly into floor, arms and legs extended.")
                        .defaultSets(3).defaultReps("30-45 sec").defaultRestSeconds(45).build(),

                // ==================== 6. SHOULDERS ====================
                // Front Delts
                Exercise.builder()
                        .name("Overhead Barbell Military Press")
                        .muscleGroup("Shoulders").subCategory("Front delts").category("Compound").equipment("Full gym")
                        .experienceLevel("All").description("Press barbell vertically overhead from collarbone to full arm lockout.")
                        .defaultSets(4).defaultReps("8-10").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Seated Dumbbell Shoulder Press")
                        .muscleGroup("Shoulders").subCategory("Front delts").category("Compound").equipment("Dumbbells")
                        .experienceLevel("All").description("Sit with back support, press dumbbells overhead in a smooth arc.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),
                Exercise.builder()
                        .name("Standing Dumbbell Front Raise")
                        .muscleGroup("Shoulders").subCategory("Front delts").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Raise dumbbells forward to eye level with controlled cadence.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Arnold Press")
                        .muscleGroup("Shoulders").subCategory("Front delts").category("Compound").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Rotate palms from facing chest at bottom to facing forward at overhead lockout.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(75).build(),

                // Side Delts
                Exercise.builder()
                        .name("Standing Dumbbell Lateral Raise")
                        .muscleGroup("Shoulders").subCategory("side delts").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Raise arms out to sides until parallel with floor, leading with elbows.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cable Lateral Raise")
                        .muscleGroup("Shoulders").subCategory("side delts").category("Isolation").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Low cable across body, continuous tension through entire lateral delt arc.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Machine Lateral Raise")
                        .muscleGroup("Shoulders").subCategory("side delts").category("Isolation").equipment("Full gym")
                        .experienceLevel("Beginner").description("Padded lever arms isolate middle deltoid without wrist strain.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Leaning Single-Arm Lateral Raise")
                        .muscleGroup("Shoulders").subCategory("side delts").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("Intermediate").description("Hold post with one hand and lean out, extending range of lateral deltoid stretch.")
                        .defaultSets(3).defaultReps("12").defaultRestSeconds(45).build(),

                // Rear Delts
                Exercise.builder()
                        .name("Bent-Over Dumbbell Rear Delt Fly")
                        .muscleGroup("Shoulders").subCategory("rear delts").category("Isolation").equipment("Dumbbells")
                        .experienceLevel("All").description("Torso hinged forward 45 degrees, raise arms in wide arc targeting posterior deltoid.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Cable Face Pull for Rear Delts")
                        .muscleGroup("Shoulders").subCategory("rear delts").category("Isolation").equipment("Full gym")
                        .experienceLevel("All").description("Rope attachment pulled to eye line with external shoulder rotation.")
                        .defaultSets(3).defaultReps("15").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Reverse Pec Deck Fly")
                        .muscleGroup("Shoulders").subCategory("rear delts").category("Isolation").equipment("Full gym")
                        .experienceLevel("Beginner").description("Sit facing machine pad, push handles outward in horizontal abduction.")
                        .defaultSets(3).defaultReps("12-15").defaultRestSeconds(60).build(),

                // Full Shoulders
                Exercise.builder()
                        .name("Barbell Push Press")
                        .muscleGroup("Shoulders").subCategory("full shoulders").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Slight dip of knees to initiate explosive overhead barbell drive.")
                        .defaultSets(3).defaultReps("6-8").defaultRestSeconds(90).build(),
                Exercise.builder()
                        .name("Barbell Upright Row")
                        .muscleGroup("Shoulders").subCategory("full shoulders").category("Compound").equipment("Full gym")
                        .experienceLevel("Intermediate").description("Pull bar vertically along torso up to chest height, elbows leading high.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Pike Push-Ups")
                        .muscleGroup("Shoulders").subCategory("full shoulders").category("Compound").equipment("Bodyweight only")
                        .experienceLevel("Intermediate").description("Hips raised high in inverted V, press downward targeting shoulder girdle.")
                        .defaultSets(3).defaultReps("10-12").defaultRestSeconds(60).build(),

                // ==================== 7. CARDIO (FAT LOSS WITH TIME) ====================
                Exercise.builder()
                        .name("Stair Climber")
                        .muscleGroup("Cardio").subCategory("Stair climber").category("Cardio").equipment("Full gym")
                        .experienceLevel("All").description("Continuous stair climbing machine maintaining upright posture and steady cardiovascular cadence.")
                        .defaultSets(1).defaultReps("25-30 mins").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Stationary Cycling Machine")
                        .muscleGroup("Cardio").subCategory("cycling machine").category("Cardio").equipment("Full gym")
                        .experienceLevel("All").description("Low-impact stationary bike cycling maintained in the target fat-burning aerobic zone.")
                        .defaultSets(1).defaultReps("30-40 mins").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Elliptical Machine")
                        .muscleGroup("Cardio").subCategory("elliptical machines").category("Cardio").equipment("Full gym")
                        .experienceLevel("All").description("Smooth low-impact cross-trainer session engaging both upper and lower body for sustained caloric expenditure.")
                        .defaultSets(1).defaultReps("25-35 mins").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Treadmill Incline Walk / Jog")
                        .muscleGroup("Cardio").subCategory("treadmill").category("Cardio").equipment("Full gym")
                        .experienceLevel("All").description("Steady-state treadmill incline walk or moderate jog optimized for lipolysis and fat loss.")
                        .defaultSets(1).defaultReps("20-30 mins").defaultRestSeconds(60).build(),
                Exercise.builder()
                        .name("Dance Cardio Session")
                        .muscleGroup("Cardio").subCategory("Dance cardio").category("Cardio").equipment("Bodyweight only")
                        .experienceLevel("All").description("High-energy rhythmic dance cardio routines boosting metabolic rate, stamina, and calorie burn.")
                        .defaultSets(1).defaultReps("30-45 mins").defaultRestSeconds(60).build()
        );

        exerciseRepository.saveAll(exercises);
    }

    private void seedTrainersAndUsers() {
        if (trainerRepository.count() == 0) {
            List<Trainer> trainers = Arrays.asList(
                    Trainer.builder()
                            .name("Marcus Vance")
                            .specialization("Strength Development & Powerlifting")
                            .experienceYears(8)
                            .email("marcus.vance@fitschedule.com")
                            .password("trainer123")
                            .rating(4.9)
                            .bio("CSCS certified strength coach specializing in heavy compound periodization and powerlifting biomechanics.")
                            .avatarUrl("https://images.unsplash.com/photo-1567013127542-490d757e51fc?w=200&fit=crop&crop=face")
                            .build(),
                    Trainer.builder()
                            .name("Sarah Jenkins")
                            .specialization("Hypertrophy & Physique Building")
                            .experienceYears(6)
                            .email("sarah.jenkins@fitschedule.com")
                            .password("trainer123")
                            .rating(4.95)
                            .bio("Exercise science specialist focused on muscle hypertrophy, progressive overload volume, and aesthetic proportion.")
                            .avatarUrl("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200&fit=crop&crop=face")
                            .build(),
                    Trainer.builder()
                            .name("David Chen")
                            .specialization("Calisthenics & Joint Mobility")
                            .experienceYears(5)
                            .email("david.chen@fitschedule.com")
                            .password("trainer123")
                            .rating(4.85)
                            .bio("Bodyweight gymnastics and mobility expert helping athletes build bulletproof joints and elite relative strength.")
                            .avatarUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&fit=crop&crop=face")
                            .build(),
                    Trainer.builder()
                            .name("Elena Rostova")
                            .specialization("Fat Loss & Metabolic Conditioning")
                            .experienceYears(7)
                            .email("elena.rostova@fitschedule.com")
                            .password("trainer123")
                            .rating(4.9)
                            .bio("High-intensity conditioning and sustainable fat loss coach specializing in metabolic work capacity.")
                            .avatarUrl("https://images.unsplash.com/photo-1580489944761-15a19d654956?w=200&fit=crop&crop=face")
                            .build()
            );
            trainerRepository.saveAll(trainers);
        } else {
            // Ensure existing trainers have password and isVerified set
            trainerRepository.findAll().forEach(t -> {
                boolean changed = false;
                if (t.getPassword() == null || t.getPassword().isBlank()) {
                    t.setPassword("trainer123");
                    changed = true;
                }
                if (t.getIsVerified() == null) {
                    t.setIsVerified(true);
                    changed = true;
                }
                if (changed) {
                    trainerRepository.save(t);
                }
            });
        }

        // Admin account (was Demo Member – now site administrator)
        seedUserIfNotExists("Demo Admin", "member@fitschedule.com", "password123", "ADMIN", null);
        seedUserIfNotExists("Marcus Vance", "marcus.vance@fitschedule.com", "trainer123", "TRAINER", "Strength Development & Powerlifting");
        seedUserIfNotExists("Marcus Vance (Coach)", "trainer@fitschedule.com", "trainer123", "TRAINER", "Strength Development & Powerlifting");
        seedUserIfNotExists("Sarah Jenkins", "sarah.jenkins@fitschedule.com", "trainer123", "TRAINER", "Hypertrophy & Physique Building");
        seedUserIfNotExists("David Chen", "david.chen@fitschedule.com", "trainer123", "TRAINER", "Calisthenics & Joint Mobility");
        seedUserIfNotExists("Elena Rostova", "elena.rostova@fitschedule.com", "trainer123", "TRAINER", "Fat Loss & Metabolic Conditioning");
    }

    private void seedUserIfNotExists(String fullName, String email, String password, String role, String specialization) {
        userRepository.findByEmailIgnoreCase(email).ifPresentOrElse(
                user -> {
                    // If member@fitschedule.com exists with old MEMBER role, elevate to ADMIN
                    if ("member@fitschedule.com".equalsIgnoreCase(email) && !"ADMIN".equalsIgnoreCase(user.getRole())) {
                        user.setRole("ADMIN");
                        user.setFullName(fullName);
                        userRepository.save(user);
                    }
                },
                () -> {
                    userRepository.save(User.builder()
                            .fullName(fullName)
                            .email(email)
                            .password(password)
                            .role(role)
                            .specialization(specialization)
                            .build());
                }
        );
    }
}
