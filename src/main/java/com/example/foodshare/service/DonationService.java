package com.example.foodshare.service;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.User;
import com.example.foodshare.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;

    public Donation createDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    public Optional<Donation> findById(UUID id) {
        return donationRepository.findById(id);
    }

    public List<Donation> findAll() {
        return donationRepository.findAll();
    }

    public List<Donation> findByDonor(User donor) {
        System.out.println("=== FINDING DONATIONS BY DONOR ===");
        System.out.println("Donor ID: " + (donor != null ? donor.getId() : "NULL"));
        
        if (donor == null) {
            System.out.println("Donor is null, returning empty list");
            return List.of();
        }
        
        List<Donation> allDonations = donationRepository.findAll();
        System.out.println("Total donations in database: " + allDonations.size());
        
        List<Donation> donorDonations = allDonations.stream()
            .filter(d -> d.getDonor() != null && d.getDonor().getId().equals(donor.getId()))
            .toList();
        
        System.out.println("Donations found for donor: " + donorDonations.size());
        return donorDonations;
    }

    public List<Donation> findByCityAndStatus(String city, String status) {
        return donationRepository.findByCityAndStatus(city, status);
    }

    public Donation updateDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    @Transactional
    public void deleteDonation(UUID id) {
        donationRepository.deleteById(id);
    }

    @Transactional
    public void deleteById(UUID id) {
        donationRepository.deleteById(id);
    }

    // Auto-expire job (invoked by scheduler)
    @Transactional
    public void expireDonations() {
        List<Donation> donations = donationRepository.findAll();
        LocalDate now = LocalDate.now();
        for (Donation d : donations) {
            if (d.getExpiryDate() != null && d.getExpiryDate().isBefore(now) && !"expired".equalsIgnoreCase(d.getStatus())) {
                d.setStatus("expired");
                donationRepository.save(d);
            }
        }
    }

    public long countTotalDonations() {
        return donationRepository.count();
    }

    public Map<String, Long> countDonationsByFoodType() {
        return donationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        donation -> {
                            String type = donation.getFoodType();
                            return type != null ? type.toUpperCase() : "UNKNOWN";
                        },
                        Collectors.counting()));
    }

    public Map<String, Long> countDonationsByStatus() {
        return donationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        donation -> {
                            String status = donation.getStatus();
                            return status != null ? status.toUpperCase() : "UNKNOWN";
                        },
                        Collectors.counting()));
    }

    public Map<String, Long> countDonationsByWeek(int weeks) {
        List<Donation> donations = donationRepository.findAll();
        Map<String, Long> weeklyCounts = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (int i = weeks - 1; i >= 0; i--) {
            LocalDate periodStart = today.minusWeeks(i);
            LocalDate periodEnd = periodStart.plusWeeks(1);
            long count = donations.stream()
                    .filter(donation -> donation.getCreatedAt() != null)
                    .map(donation -> donation.getCreatedAt()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .filter(date -> !date.isBefore(periodStart) && date.isBefore(periodEnd))
                    .count();
            String label;
            if (i == 0) {
                label = "This Week";
            } else {
                String month = periodStart.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
                int weekNumber = periodStart.get(weekFields.weekOfWeekBasedYear());
                label = month + " W" + weekNumber;
            }
            weeklyCounts.put(label, count);
        }
        return weeklyCounts;
    }

    public Map<String, Long> countMonthlyDonations(int months) {
        List<Donation> donations = donationRepository.findAll();
        Map<String, Long> monthlyCounts = new LinkedHashMap<>();
        YearMonth current = YearMonth.now();

        for (int i = months - 1; i >= 0; i--) {
            YearMonth target = current.minusMonths(i);
            long count = donations.stream()
                    .filter(donation -> donation.getCreatedAt() != null)
                    .map(donation -> YearMonth.from(donation.getCreatedAt()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()))
                    .filter(ym -> ym.equals(target))
                    .count();
            String label = target.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            monthlyCounts.put(label, count);
        }

        return monthlyCounts;
    }

    public Map<String, Long> topDonorsByDonationCount(int limit) {
        return donationRepository.findAll().stream()
                .filter(donation -> donation.getDonor() != null && donation.getDonor().getName() != null)
                .collect(Collectors.groupingBy(donation -> donation.getDonor().getName(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    // Additional analytics/statistics methods as needed
}
