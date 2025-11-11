package com.example.foodshare.service;

import com.example.foodshare.entity.DonationRequest;
import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.User;
import com.example.foodshare.repository.DonationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationRequestService {
    private final DonationRequestRepository donationRequestRepository;

    public DonationRequest createRequest(DonationRequest request) {
        return donationRequestRepository.save(request);
    }

    public Optional<DonationRequest> findById(UUID id) {
        return donationRequestRepository.findById(id);
    }

    public List<DonationRequest> getByDonation(Donation donation) {
        return donationRequestRepository.findAll().stream()
                .filter(r -> r.getDonation().equals(donation))
                .toList();
    }

    public List<DonationRequest> getByReceiver(User receiver) {
        return donationRequestRepository.findAll().stream()
                .filter(r -> r.getReceiver().equals(receiver))
                .toList();
    }

    public DonationRequest updateRequest(DonationRequest request) {
        return donationRequestRepository.save(request);
    }

    public void approveRequest(DonationRequest request) {
        request.setStatus("approved");
        donationRequestRepository.save(request);
    }
    public void markCompleted(DonationRequest request) {
        request.setStatus("completed");
        donationRequestRepository.save(request);
    }
}
