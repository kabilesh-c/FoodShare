package com.example.foodshare.service;

import com.example.foodshare.entity.User;
import com.example.foodshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long countAll() {
        return userRepository.count();
    }

    public long countByRole(String role) {
        if (role == null || role.isBlank()) {
            return 0;
        }
        String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
        return userRepository.countByRole(normalized);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUserDetails(UUID id, String name, String role, String city, String phone) {
        return userRepository.findById(id)
                .map(existing -> {
                    if (name != null && !name.isBlank()) {
                        existing.setName(name.trim());
                    }
                    if (city != null) {
                        existing.setCity(city.trim());
                    }
                    if (phone != null) {
                        existing.setPhone(phone.trim());
                    }
                    if (role != null && !role.isBlank()) {
                        String normalizedRole = role.startsWith("ROLE_")
                                ? role
                                : "ROLE_" + role.trim().toUpperCase();
                        existing.setRole(normalizedRole);
                    }
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found for id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== LOADING USER ===");
        System.out.println("Email: " + email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found. Email: " + email));
        
        System.out.println("Found user: " + user.getName());
        System.out.println("User role from DB: " + user.getRole());
        
        // Extract role name without ROLE_ prefix for Spring Security
        String role = user.getRole();
        if (role.startsWith("ROLE_")) {
            role = role.substring(5); // Remove "ROLE_" prefix
        }
        
        System.out.println("Role after processing: " + role);
        System.out.println("Final authorities will be: ROLE_" + role);
        
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(role)
                .build();
    }
}
