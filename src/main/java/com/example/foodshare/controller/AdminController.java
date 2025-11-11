package com.example.foodshare.controller;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.Report;
import com.example.foodshare.entity.User;
import com.example.foodshare.service.DonationService;
import com.example.foodshare.service.ReportService;
import com.example.foodshare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final DonationService donationService;
    private final ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        System.out.println("=== ADMIN DASHBOARD ===");
        
        try {
            List<Donation> donations = donationService.findAll();
            long totalDonations = donations.size();
            long activeUsers = userService.countAll();
            var reports = reportService.getAllReports();

            Map<String, Long> weeklyCounts = donationService.countDonationsByWeek(4);
            Map<String, Long> foodTypeCounts = donationService.countDonationsByFoodType();

            model.addAttribute("totalDonations", totalDonations);
            model.addAttribute("activeUsers", activeUsers);
            model.addAttribute("totalFoodSaved", reportService.getTotalFoodSaved());
            model.addAttribute("totalBeneficiaries", reportService.getTotalReceiversServed());
            model.addAttribute("donations", donations);
            model.addAttribute("weeklyLabels", new ArrayList<>(weeklyCounts.keySet()));
            model.addAttribute("weeklyValues", new ArrayList<>(weeklyCounts.values()));
            model.addAttribute("foodTypeLabels", new ArrayList<>(foodTypeCounts.keySet()));
            model.addAttribute("foodTypeValues", new ArrayList<>(foodTypeCounts.values()));
            model.addAttribute("recentReports", reports.stream().limit(5).toList());
            
            System.out.println("Returning admin/dashboard template");
            return "admin/dashboard";
        } catch (Exception e) {
            System.err.println("ERROR in admin dashboard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String users(Model model) {
        List<User> allUsers = userService.findAll();
        model.addAttribute("users", allUsers);
        model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_DONOR", "ROLE_RECEIVER"));
        
        // Calculate real-time statistics
        long totalUsers = allUsers.size();
        long activeDonors = allUsers.stream().filter(u -> "ROLE_DONOR".equals(u.getRole())).count();
        long receivers = allUsers.stream().filter(u -> "ROLE_RECEIVER".equals(u.getRole())).count();
        long admins = allUsers.stream().filter(u -> "ROLE_ADMIN".equals(u.getRole())).count();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeDonors", activeDonors);
        model.addAttribute("receivers", receivers);
        model.addAttribute("admins", admins);
        
        return "admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/donations")
    public String donations(Model model) {
        System.out.println("=== ADMIN DONATIONS ===");
        
        try {
            List<Donation> donations = donationService.findAll();
            System.out.println("Total donations found: " + (donations != null ? donations.size() : 0));
            
            model.addAttribute("donations", donations);
            model.addAttribute("statusCounts", donationService.countDonationsByStatus());
            
            System.out.println("Returning admin/donations template");
            return "admin/donations";
        } catch (Exception e) {
            System.err.println("ERROR in admin donations: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reports")
    public String reports(Model model) {
        List<Report> reports = reportService.getAllReports();
        Map<String, Long> monthlyCounts = donationService.countMonthlyDonations(6);
        Map<String, Long> topDonors = donationService.topDonorsByDonationCount(5);

        model.addAttribute("reports", reports);
        model.addAttribute("monthlyLabels", new ArrayList<>(monthlyCounts.keySet()));
        model.addAttribute("monthlyValues", new ArrayList<>(monthlyCounts.values()));
        model.addAttribute("topDonors", topDonors);
        model.addAttribute("totalFoodSaved", reportService.getTotalFoodSaved());
        model.addAttribute("totalReportedDonations", reportService.getTotalReportedDonations());
        model.addAttribute("totalReceiversServed", reportService.getTotalReceiversServed());
        return "admin/reports";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/analytics")
    public String analytics(Model model) {
        // Analytics is essentially the same as reports but with a different view focus
        List<Report> reports = reportService.getAllReports();
        Map<String, Long> monthlyCounts = donationService.countMonthlyDonations(12);
        Map<String, Long> topDonors = donationService.topDonorsByDonationCount(10);
        Map<String, Long> foodTypeCounts = donationService.countDonationsByFoodType();
        Map<String, Long> weeklyCounts = donationService.countDonationsByWeek(8);

        model.addAttribute("reports", reports);
        model.addAttribute("monthlyLabels", new ArrayList<>(monthlyCounts.keySet()));
        model.addAttribute("monthlyValues", new ArrayList<>(monthlyCounts.values()));
        model.addAttribute("weeklyLabels", new ArrayList<>(weeklyCounts.keySet()));
        model.addAttribute("weeklyValues", new ArrayList<>(weeklyCounts.values()));
        model.addAttribute("topDonors", topDonors);
        model.addAttribute("foodTypeLabels", new ArrayList<>(foodTypeCounts.keySet()));
        model.addAttribute("foodTypeValues", new ArrayList<>(foodTypeCounts.values()));
        model.addAttribute("totalFoodSaved", reportService.getTotalFoodSaved());
        model.addAttribute("totalReportedDonations", reportService.getTotalReportedDonations());
        model.addAttribute("totalReceiversServed", reportService.getTotalReceiversServed());
        return "admin/analytics";
    }

    @GetMapping("/reports/export")
    public ResponseEntity<Resource> exportReports() {
        String csvData = reportService.exportReportsToCSV();
        ByteArrayResource resource = new ByteArrayResource(csvData.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=foodshare-reports.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable UUID id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        model.addAttribute("user", user);
        model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_DONOR", "ROLE_RECEIVER"));
        return "admin/user-edit";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable UUID id,
                             @RequestParam String name,
                             @RequestParam String role,
                             @RequestParam(required = false) String city,
                             @RequestParam(required = false) String phone,
                             RedirectAttributes redirectAttributes) {
        userService.updateUserDetails(id, name, role, city, phone);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable UUID id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (authentication != null && authentication.getName().equalsIgnoreCase(user.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete the account that is currently signed in.");
            return "redirect:/admin/users";
        }

        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        return "redirect:/admin/users";
    }
}
