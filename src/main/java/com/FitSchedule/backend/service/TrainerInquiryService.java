package com.FitSchedule.backend.service;

import com.FitSchedule.backend.entity.TrainerInquiry;
import com.FitSchedule.backend.repository.TrainerInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerInquiryService {

    private final TrainerInquiryRepository trainerInquiryRepository;

    @Transactional
    public TrainerInquiry createInquiry(TrainerInquiry inquiry) {
        inquiry.setSubmittedAt(LocalDateTime.now());
        if (inquiry.getStatus() == null) {
            inquiry.setStatus("SUBMITTED");
        }
        return trainerInquiryRepository.save(inquiry);
    }

    @Transactional(readOnly = true)
    public List<TrainerInquiry> getAllInquiries() {
        return trainerInquiryRepository.findAllByOrderBySubmittedAtDesc();
    }

    @Transactional
    public TrainerInquiry updateStatus(Long id, String status) {
        TrainerInquiry inquiry = trainerInquiryRepository.findById(id)
                .orElseThrow(() -> new com.FitSchedule.backend.exception.ResourceNotFoundException("Inquiry not found with ID: " + id));
        inquiry.setStatus(status);
        return trainerInquiryRepository.save(inquiry);
    }
}
