package com.FitSchedule.backend.repository;

import com.FitSchedule.backend.entity.WorkoutDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, Long> {
    List<WorkoutDay> findByWorkoutPlanIdOrderByDayOrderAsc(Long planId);
}
