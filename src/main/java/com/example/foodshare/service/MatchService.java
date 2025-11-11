package com.example.foodshare.service;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.Match;
import com.example.foodshare.entity.User;
import com.example.foodshare.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {
    private final MatchRepository matchRepository;

    public Match createMatch(Donation donation, User receiver) {
        Match match = Match.builder()
                .donation(donation)
                .receiver(receiver)
                .build();
        return matchRepository.save(match);
    }

    public List<Match> getMatchesByReceiver(User receiver) {
        return matchRepository.findAll().stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .toList();
    }

    public List<Match> getMatchesByDonation(Donation donation) {
        return matchRepository.findAll().stream()
                .filter(m -> m.getDonation().equals(donation))
                .toList();
    }

    public Optional<Match> findById(UUID id) {
        return matchRepository.findById(id);
    }
}
