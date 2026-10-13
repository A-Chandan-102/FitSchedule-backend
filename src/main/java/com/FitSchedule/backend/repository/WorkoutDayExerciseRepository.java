package com.FitSchedule.backend.repository;

import com.FitSchedule.backend.entity.WorkoutDayExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDayExerciseRepository extends JpaRepository<WorkoutDayExercise, Long> {
    List<WorkoutDayExercise> findByWorkoutDayIdOrderByOrderIndexAsc(Long dayId);
}
