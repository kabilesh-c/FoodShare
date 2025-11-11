package com.example.foodshare.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        System.out.println("=== DASHBOARD CONTROLLER ===");
        System.out.println("Authentication: " + authentication);
        
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("User is authenticated: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
            
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();
                System.out.println("Checking role: " + role);
                
                switch (role) {
                    case "ROLE_ADMIN":
                        System.out.println("Redirecting to ADMIN dashboard");
                        return "redirect:/admin/dashboard";
                    case "ROLE_DONOR":
                        System.out.println("Redirecting to DONOR dashboard");
                        return "redirect:/donor/dashboard";
                    case "ROLE_RECEIVER":
                        System.out.println("Redirecting to RECEIVER dashboard");
                        return "redirect:/receiver/dashboard";
                }
            }
            System.out.println("No matching role found!");
        } else {
            System.out.println("User is NOT authenticated");
        }
        return "redirect:/login";
    }
    
    @GetMapping("/developer")
    public String developerPage() {
        return "developer";
    }
}
