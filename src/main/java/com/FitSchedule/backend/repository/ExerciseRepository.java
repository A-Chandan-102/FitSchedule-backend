package com.FitSchedule.backend.repository;

import com.FitSchedule.backend.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMuscleGroupIgnoreCase(String muscleGroup);
    List<Exercise> findByEquipmentIgnoreCase(String equipment);
    List<Exercise> findByMuscleGroupInIgnoreCase(List<String> muscleGroups);
}
