package com.example.foodshare.controller;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.User;
import com.example.foodshare.service.DonationService;
import com.example.foodshare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/donor")
@RequiredArgsConstructor
@Slf4j
public class DonorController {
    private final DonationService donationService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        System.out.println("=== DONOR DASHBOARD ===");
        System.out.println("Principal: " + principal);
        System.out.println("Principal name: " + (principal != null ? principal.getName() : "NULL"));
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            System.out.println("Found donor: " + donor);
            
            List<Donation> allDonations = donationService.findByDonor(donor);
            
            // Calculate real-time statistics
            int totalDonations = allDonations != null ? allDonations.size() : 0;
            
            // Calculate total food saved (sum of all quantities, assuming kg)
            double totalFoodSaved = allDonations != null ? 
                allDonations.stream()
                    .mapToDouble(d -> {
                        try {
                            String qty = d.getQuantity();
                            if (qty != null) {
                                // Extract number from quantity string (e.g., "10kg" -> 10)
                                String numStr = qty.replaceAll("[^0-9.]", "");
                                return numStr.isEmpty() ? 5.0 : Double.parseDouble(numStr);
                            }
                            return 5.0; // default 5kg per donation
                        } catch (Exception e) {
                            return 5.0;
                        }
                    })
                    .sum() : 0;
            
            // Estimate people fed (assume 1kg feeds 2 people)
            int peopleFed = (int) (totalFoodSaved * 2);
            
            // Calculate success rate (claimed donations / total donations)
            long claimedCount = allDonations != null ? 
                allDonations.stream().filter(d -> "claimed".equalsIgnoreCase(d.getStatus())).count() : 0;
            int successRate = totalDonations > 0 ? (int) ((claimedCount * 100.0) / totalDonations) : 0;
            
            // Count by food type for charts
            long vegCount = allDonations != null ? 
                allDonations.stream().filter(d -> "VEG".equalsIgnoreCase(d.getFoodType())).count() : 0;
            long nonVegCount = allDonations != null ? 
                allDonations.stream().filter(d -> "NON-VEG".equalsIgnoreCase(d.getFoodType())).count() : 0;
            long otherCount = allDonations != null ? 
                allDonations.stream().filter(d -> "VEGAN".equalsIgnoreCase(d.getFoodType()) || 
                                                      "OTHER".equalsIgnoreCase(d.getFoodType())).count() : 0;
            
            model.addAttribute("donor", donor);
            model.addAttribute("stats", allDonations);
            model.addAttribute("donations", allDonations.stream().limit(6).toList()); // Recent 6 donations
            model.addAttribute("totalDonations", totalDonations);
            model.addAttribute("totalFoodSaved", String.format("%.0f", totalFoodSaved));
            model.addAttribute("peopleFed", peopleFed);
            model.addAttribute("successRate", successRate);
            model.addAttribute("vegCount", vegCount);
            model.addAttribute("nonVegCount", nonVegCount);
            model.addAttribute("otherCount", otherCount);
            
            System.out.println("Stats - Total: " + totalDonations + ", Food Saved: " + totalFoodSaved + "kg, People Fed: " + peopleFed + ", Success: " + successRate + "%");
            System.out.println("Returning donor/dashboard template");
            return "donor/dashboard";
        } catch (Exception e) {
            System.err.println("ERROR in donor dashboard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/add")
    public String addDonationPage(Model model) {
        model.addAttribute("donation", new Donation());
        return "donor/addDonation";
    }

    @PostMapping("/add")
    public String addDonation(@Valid @ModelAttribute("donation") Donation donation, BindingResult result,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, 
                             Principal principal, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("=== ADD DONATION POST ===");
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        System.out.println("Donation object: " + donation);
        System.out.println("Image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "NULL"));
        System.out.println("Binding result has errors: " + result.hasErrors());
        
        if(result.hasErrors()) {
            System.err.println("Validation errors:");
            result.getAllErrors().forEach(error -> System.err.println("- " + error.getDefaultMessage()));
            model.addAttribute("donation", donation);
            return "donor/addDonation";
        }
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            System.out.println("Found donor: " + donor);
            
            if (donor == null) {
                System.err.println("ERROR: Donor not found for email: " + principal.getName());
                model.addAttribute("error", "User not found. Please login again.");
                model.addAttribute("donation", donation);
                return "donor/addDonation";
            }
            
            // Set donor relationship
            donation.setDonor(donor);
            
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    // For now, just set a placeholder URL. In production, save to file system or cloud storage
                    donation.setImageUrl("/images/uploads/" + imageFile.getOriginalFilename());
                    System.out.println("Set image URL: " + donation.getImageUrl());
                    // TODO: Implement actual file saving logic
                } catch (Exception e) {
                    System.err.println("Error handling image upload: " + e.getMessage());
                    // Continue without image if upload fails
                }
            }
            
            // Set default values
            donation.setStatus("available");
            donation.setCreatedAt(java.time.Instant.now());
            
            System.out.println("Saving donation: " + donation);
            Donation savedDonation = donationService.createDonation(donation);
            System.out.println("Saved donation with ID: " + savedDonation.getId());
            
            // Add success message to redirect attributes
            redirectAttributes.addFlashAttribute("successMessage", "✅ Food donation added successfully! Thank you for sharing.");
            
            return "redirect:/donor/my";
        } catch (Exception e) {
            System.err.println("ERROR saving donation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to save donation. Please try again.");
            model.addAttribute("donation", donation);
            return "donor/addDonation";
        }
    }

    @GetMapping("/my")
    public String myDonations(Model model, Principal principal) {
        System.out.println("=== MY DONATIONS ===");
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        
        if (principal == null) {
            return "redirect:/login";
        }
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            System.out.println("Found donor: " + donor);
            
            if (donor == null) {
                System.err.println("ERROR: Donor not found for email: " + principal.getName());
                return "redirect:/login";
            }
            
            List<Donation> donations = donationService.findByDonor(donor);
            System.out.println("Found donations: " + (donations != null ? donations.size() : 0));
            
            model.addAttribute("donations", donations);
            model.addAttribute("donor", donor);
            
            System.out.println("Returning donor/myDonations template");
            return "donor/myDonations";
        } catch (Exception e) {
            System.err.println("ERROR in myDonations: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to load donations. Please try again.");
            return "donor/myDonations";
        }
    }

    @GetMapping("/edit/{id}")
    public String editDonationPage(@PathVariable("id") java.util.UUID id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        System.out.println("=== EDIT DONATION PAGE ===");
        System.out.println("Donation ID: " + id);
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        
        if (principal == null) {
            return "redirect:/login";
        }
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            if (donor == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/login";
            }
            
            Donation donation = donationService.findById(id).orElse(null);
            if (donation == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Donation not found.");
                return "redirect:/donor/my";
            }
            
            // Check if the donation belongs to the current donor
            if (!donation.getDonor().getId().equals(donor.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to edit this donation.");
                return "redirect:/donor/my";
            }
            
            model.addAttribute("donation", donation);
            System.out.println("Returning donor/editDonation template");
            return "donor/editDonation";
        } catch (Exception e) {
            System.err.println("ERROR in editDonationPage: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to load donation. Please try again.");
            return "redirect:/donor/my";
        }
    }

    @PostMapping("/edit/{id}")
    public String editDonation(@PathVariable("id") java.util.UUID id, 
                              @Valid @ModelAttribute("donation") Donation donation, 
                              BindingResult result,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Principal principal, 
                              Model model, 
                              RedirectAttributes redirectAttributes) {
        System.out.println("=== EDIT DONATION POST ===");
        System.out.println("Donation ID: " + id);
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        
        if (result.hasErrors()) {
            System.err.println("Validation errors:");
            result.getAllErrors().forEach(error -> System.err.println("- " + error.getDefaultMessage()));
            model.addAttribute("donation", donation);
            return "donor/editDonation";
        }
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            if (donor == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/login";
            }
            
            Donation existingDonation = donationService.findById(id).orElse(null);
            if (existingDonation == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Donation not found.");
                return "redirect:/donor/my";
            }
            
            // Check if the donation belongs to the current donor
            if (!existingDonation.getDonor().getId().equals(donor.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to edit this donation.");
                return "redirect:/donor/my";
            }
            
            // Update donation fields
            existingDonation.setFoodName(donation.getFoodName());
            existingDonation.setDescription(donation.getDescription());
            existingDonation.setFoodType(donation.getFoodType());
            existingDonation.setQuantity(donation.getQuantity());
            existingDonation.setExpiryDate(donation.getExpiryDate());
            existingDonation.setCity(donation.getCity());
            
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    existingDonation.setImageUrl("/images/uploads/" + imageFile.getOriginalFilename());
                    System.out.println("Updated image URL: " + existingDonation.getImageUrl());
                } catch (Exception e) {
                    System.err.println("Error handling image upload: " + e.getMessage());
                }
            }
            
            System.out.println("Updating donation: " + existingDonation);
            donationService.updateDonation(existingDonation);
            
            redirectAttributes.addFlashAttribute("successMessage", "✅ Donation updated successfully!");
            return "redirect:/donor/my";
        } catch (Exception e) {
            System.err.println("ERROR updating donation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to update donation. Please try again.");
            model.addAttribute("donation", donation);
            return "donor/editDonation";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDonation(@PathVariable("id") java.util.UUID id, Principal principal, RedirectAttributes redirectAttributes) {
        System.out.println("=== DELETE DONATION ===");
        System.out.println("Donation ID: " + id);
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        
        if (principal == null) {
            return "redirect:/login";
        }
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            if (donor == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found. Please login again.");
                return "redirect:/login";
            }
            
            Donation donation = donationService.findById(id).orElse(null);
            if (donation == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Donation not found.");
                return "redirect:/donor/my";
            }
            
            // Check if the donation belongs to the current donor
            if (!donation.getDonor().getId().equals(donor.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to delete this donation.");
                return "redirect:/donor/my";
            }
            
            donationService.deleteDonation(id);
            System.out.println("Deleted donation with ID: " + id);
            
            redirectAttributes.addFlashAttribute("successMessage", "✅ Donation deleted successfully!");
            return "redirect:/donor/my";
        } catch (Exception e) {
            System.err.println("ERROR deleting donation: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete donation. Please try again.");
            return "redirect:/donor/my";
        }
    }

    @GetMapping("/analytics")
    public String analytics(Model model, Principal principal) {
        System.out.println("=== DONOR ANALYTICS ===");
        System.out.println("Principal: " + (principal != null ? principal.getName() : "NULL"));
        
        if (principal == null) {
            return "redirect:/login";
        }
        
        try {
            User donor = userService.findByEmail(principal.getName()).orElse(null);
            var donations = donationService.findByDonor(donor);
            
            System.out.println("Found donor: " + donor);
            System.out.println("Total donations count: " + (donations != null ? donations.size() : 0));
            
            model.addAttribute("donor", donor);
            model.addAttribute("donations", donations);
            model.addAttribute("totalDonations", donations != null ? donations.size() : 0);
            
            // Calculate stats
            if (donations != null && !donations.isEmpty()) {
                long vegCount = donations.stream().filter(d -> "Veg".equalsIgnoreCase(d.getFoodType())).count();
                long nonVegCount = donations.stream().filter(d -> "Non-Veg".equalsIgnoreCase(d.getFoodType())).count();
                long otherCount = donations.stream().filter(d -> "Other".equalsIgnoreCase(d.getFoodType())).count();
                
                model.addAttribute("vegCount", vegCount);
                model.addAttribute("nonVegCount", nonVegCount);
                model.addAttribute("otherCount", otherCount);
                
                // Estimate people fed (assuming 1 donation feeds ~5 people)
                model.addAttribute("peopleFed", donations.size() * 5);
                
                System.out.println("Stats - Veg: " + vegCount + ", Non-Veg: " + nonVegCount + ", Other: " + otherCount);
            } else {
                model.addAttribute("vegCount", 0);
                model.addAttribute("nonVegCount", 0);
                model.addAttribute("otherCount", 0);
                model.addAttribute("peopleFed", 0);
            }
            
            // Add empty feedbacks for now
            model.addAttribute("feedbacks", java.util.Collections.emptyList());
            
            System.out.println("Returning donor/analytics template");
            return "donor/analytics";
        } catch (Exception e) {
            System.err.println("ERROR in analytics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
