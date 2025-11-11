package com.example.foodshare.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes for demo users
 * Run this class to generate hashes for password123
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "password123";
        String hash = encoder.encode(password);
        
        System.out.println("===========================================");
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("===========================================");
        
        // Test the hash
        boolean matches = encoder.matches(password, hash);
        System.out.println("Hash verification: " + (matches ? "✓ PASS" : "✗ FAIL"));
        
        // Generate a consistent hash for data.sql
        String consistentHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye/RXEkYJUVXD8Hb0ZWVLF8lKXC4wJVa2";
        boolean consistentMatch = encoder.matches(password, consistentHash);
        System.out.println("\nConsistent hash test: " + (consistentMatch ? "✓ PASS" : "✗ FAIL"));
    }
}
