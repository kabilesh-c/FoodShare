package com.example.foodshare.controller;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;

    @GetMapping("")
    public String allDonations(Model model) {
        List<Donation> donations = donationService.findAll();
        model.addAttribute("donations", donations);
        return "donations/all";
    }

    @GetMapping("/status/{status}")
    public String donationsByStatus(@PathVariable String status, Model model) {
        List<Donation> donations = donationService.findAll().stream()
            .filter(d -> status.equalsIgnoreCase(d.getStatus()))
            .toList();
        model.addAttribute("donations", donations);
        return "donations/all";
    }

    @PostMapping("/markClaimed/{id}")
    @PreAuthorize("hasRole('DONOR') or hasRole('ADMIN')")
    public String markClaimed(@PathVariable UUID id) {
        Donation donation = donationService.findById(id).orElse(null);
        if (donation == null) return "redirect:/donations";
        donation.setStatus("claimed");
        donationService.updateDonation(donation);
        return "redirect:/donor/my";
    }
    // Other shared operations as needed
}
