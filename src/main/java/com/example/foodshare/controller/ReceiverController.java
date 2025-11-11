package com.example.foodshare.controller;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.DonationRequest;
import com.example.foodshare.entity.User;
import com.example.foodshare.service.DonationRequestService;
import com.example.foodshare.service.DonationService;
import com.example.foodshare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/receiver")
@RequiredArgsConstructor
public class ReceiverController {
    private final DonationService donationService;
    private final UserService userService;
    private final DonationRequestService donationRequestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        System.out.println("=== RECEIVER DASHBOARD ===");
        System.out.println("Principal: " + principal);
        System.out.println("Principal name: " + (principal != null ? principal.getName() : "NULL"));
        
        try {
            User receiver = userService.findByEmail(principal.getName()).orElse(null);
            System.out.println("Found receiver: " + receiver);
            
            List<Donation> donations = donationService.findByCityAndStatus(receiver.getCity(), "available");
            System.out.println("Found donations: " + (donations != null ? donations.size() : 0));
            
            // Calculate real-time statistics
            List<DonationRequest> myRequests = donationRequestService.getByReceiver(receiver);
            
            // Count claimed food (completed requests)
            long foodClaimed = myRequests != null ? 
                myRequests.stream().filter(r -> "completed".equalsIgnoreCase(r.getStatus()) || 
                                                 "approved".equalsIgnoreCase(r.getStatus())).count() : 0;
            
            // Count pending requests
            long requestsPending = myRequests != null ? 
                myRequests.stream().filter(r -> "pending".equalsIgnoreCase(r.getStatus())).count() : 0;
            
            // Count available nearby donations
            int availableNearby = donations != null ? donations.size() : 0;
            
            model.addAttribute("donations", donations);
            model.addAttribute("receiver", receiver);
            model.addAttribute("foodClaimed", foodClaimed);
            model.addAttribute("requestsPending", requestsPending);
            model.addAttribute("availableNearby", availableNearby);
            
            System.out.println("Stats - Claimed: " + foodClaimed + ", Pending: " + requestsPending + ", Available: " + availableNearby);
            System.out.println("Returning receiver/dashboard template");
            return "receiver/dashboard";
        } catch (Exception e) {
            System.err.println("ERROR in receiver dashboard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @GetMapping("/available")
    public String available(@RequestParam(required = false) String city,
                           @RequestParam(required = false) String foodType,
                           Model model, Principal principal) {
        User receiver = userService.findByEmail(principal.getName()).orElse(null);
        String filterCity = city != null ? city : receiver.getCity();
        List<Donation> donations = donationService.findByCityAndStatus(filterCity, "available");
        if (foodType != null && !foodType.isEmpty()) {
            donations = donations.stream().filter(d -> foodType.equalsIgnoreCase(d.getFoodType())).toList();
        }
        model.addAttribute("donations", donations);
        model.addAttribute("receiver", receiver);
        return "receiver/available";
    }

    @PostMapping("/claim/{donationId}")
    public String claimDonation(@PathVariable UUID donationId, Principal principal, RedirectAttributes redirectAttributes) {
        System.out.println("=== CLAIMING DONATION ===");
        System.out.println("Donation ID: " + donationId);
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        
        try {
            User receiver = userService.findByEmail(principal.getName()).orElse(null);
            if (receiver == null) {
                System.err.println("ERROR: Receiver not found");
                redirectAttributes.addFlashAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/receiver/dashboard";
            }
            
            Donation donation = donationService.findById(donationId).orElse(null);
            if (donation == null) {
                System.err.println("ERROR: Donation not found");
                redirectAttributes.addFlashAttribute("errorMessage", "Donation not found.");
                return "redirect:/receiver/dashboard";
            }
            
            if (!"available".equalsIgnoreCase(donation.getStatus())) {
                System.err.println("ERROR: Donation not available. Status: " + donation.getStatus());
                redirectAttributes.addFlashAttribute("errorMessage", "This donation is no longer available.");
                return "redirect:/receiver/dashboard";
            }
            
            // Create receiver request (DonationRequest)
            DonationRequest request = DonationRequest.builder()
                .receiver(receiver)
                .donation(donation)
                .status("pending")
                .build();
            donationRequestService.createRequest(request);
            
            // Update donation status to claimed
            donation.setStatus("claimed");
            donationService.updateDonation(donation);
            
            System.out.println("Successfully claimed donation");
            redirectAttributes.addFlashAttribute("successMessage", "✅ Donation claimed successfully! You can track it in your history.");
            
            return "redirect:/receiver/history";
        } catch (Exception e) {
            System.err.println("ERROR claiming donation: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to claim donation. Please try again.");
            return "redirect:/receiver/dashboard";
        }
    }

    @GetMapping("/history")
    public String history(Model model, Principal principal) {
        User receiver = userService.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("requests", donationRequestService.getByReceiver(receiver));
        return "receiver/history";
    }
    // Feedback endpoints would go here
}
