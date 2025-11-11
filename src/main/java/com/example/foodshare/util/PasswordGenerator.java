package com.example.foodshare.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate passwords for demo users
        String adminPassword = "admin123";
        String donorPassword = "donor123";
        String receiverPassword = "receiver123";
        
        System.out.println("Admin password hash: " + encoder.encode(adminPassword));
        System.out.println("Donor password hash: " + encoder.encode(donorPassword));
        System.out.println("Receiver password hash: " + encoder.encode(receiverPassword));
        
        // Test if the existing hashes match
        String adminHash = "$2a$10$t.jSU/PXikFjcIhU2TSOEOGZl1J0i4y3aLjxj7mFCCycXsx2ce4JW";
        String donorHash = "$2a$10$jXwzUZWhnQLeQoqHuvsLGe1ITEPrf4pRcgkl5cr0E5FfKx4/5GEl.";
        String receiverHash = "$2a$10$mgohx2Ugz/u/ftJFholPvenwCzn5PYzPifgWITp2z2g4.K4KeNE/q";
        
        System.out.println("\nTesting existing hashes:");
        System.out.println("Admin password matches: " + encoder.matches(adminPassword, adminHash));
        System.out.println("Donor password matches: " + encoder.matches(donorPassword, donorHash));
        System.out.println("Receiver password matches: " + encoder.matches(receiverPassword, receiverHash));
    }
}
