package com.example.foodshare.service;

import com.example.foodshare.entity.Report;
import com.example.foodshare.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;

    public Report createOrUpdateDailyReport(int donations, BigDecimal foodSaved, int receivers) {
        LocalDate today = LocalDate.now();
        Optional<Report> existing = reportRepository.findAll().stream()
                .filter(r -> r.getReportDate().equals(today)).findFirst();
        Report report = existing.orElseGet(() -> Report.builder()
                .reportDate(today)
                .build());
        report.setTotalDonations(donations);
        report.setTotalFoodSavedKg(foodSaved);
        report.setTotalReceiversServed(receivers);
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public BigDecimal getTotalFoodSaved() {
        return reportRepository.findAll().stream()
                .map(report -> report.getTotalFoodSavedKg() != null ? report.getTotalFoodSavedKg() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int getTotalReceiversServed() {
    return reportRepository.findAll().stream()
        .mapToInt(Report::getTotalReceiversServed)
        .sum();
    }

    public int getTotalReportedDonations() {
    return reportRepository.findAll().stream()
        .mapToInt(Report::getTotalDonations)
        .sum();
    }

    public Optional<Report> getReportById(UUID id) {
        return reportRepository.findById(id);
    }

    @Transactional
    public void deleteReport(UUID id) {
        reportRepository.deleteById(id);
    }

    public String exportReportsToCSV() {
        List<Report> reports = getAllReports();
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Report Date,Total Donations,Total Food Saved (kg),Receivers Served\n");
        for (Report report : reports) {
            LocalDate date = report.getReportDate() != null ? report.getReportDate() : LocalDate.now();
            BigDecimal foodSaved = report.getTotalFoodSavedKg() != null ? report.getTotalFoodSavedKg() : BigDecimal.ZERO;
            csvBuilder
                    .append(date)
                    .append(',')
                    .append(report.getTotalDonations())
                    .append(',')
                    .append(foodSaved)
                    .append(',')
                    .append(report.getTotalReceiversServed())
                    .append('\n');
        }
        return csvBuilder.toString();
    }
}
