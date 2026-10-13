package com.FitSchedule.backend.repository;

import com.FitSchedule.backend.entity.TrainerInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerInquiryRepository extends JpaRepository<TrainerInquiry, Long> {
    List<TrainerInquiry> findAllByOrderBySubmittedAtDesc();
}
