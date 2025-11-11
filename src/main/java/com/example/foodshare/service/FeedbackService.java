package com.example.foodshare.service;

import com.example.foodshare.entity.Feedback;
import com.example.foodshare.entity.User;
import com.example.foodshare.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Optional<Feedback> findById(UUID id) {
        return feedbackRepository.findById(id);
    }

    public List<Feedback> getByDonor(User donor) {
        return feedbackRepository.findAll().stream()
                .filter(f -> f.getDonor().equals(donor))
                .toList();
    }

    public List<Feedback> getByReceiver(User receiver) {
        return feedbackRepository.findAll().stream()
                .filter(f -> f.getReceiver().equals(receiver))
                .toList();
    }

    public List<Feedback> getAll() {
        return feedbackRepository.findAll();
    }
}
