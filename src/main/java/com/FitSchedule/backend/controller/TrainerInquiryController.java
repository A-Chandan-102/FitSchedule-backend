package com.FitSchedule.backend.controller;

import com.FitSchedule.backend.entity.TrainerInquiry;
import com.FitSchedule.backend.service.TrainerInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainer-inquiries")
@RequiredArgsConstructor
public class TrainerInquiryController {

    private final TrainerInquiryService trainerInquiryService;

    @PostMapping
    public ResponseEntity<TrainerInquiry> submitInquiry(@Valid @RequestBody TrainerInquiry inquiry) {
        TrainerInquiry created = trainerInquiryService.createInquiry(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TrainerInquiry>> getAllInquiries() {
        return ResponseEntity.ok(trainerInquiryService.getAllInquiries());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TrainerInquiry> updateStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        String status = body.getOrDefault("status", "REVIEWED");
        TrainerInquiry updated = trainerInquiryService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
