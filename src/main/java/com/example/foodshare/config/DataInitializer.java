package com.example.foodshare.config;

import com.example.foodshare.entity.Donation;
import com.example.foodshare.entity.Report;
import com.example.foodshare.entity.User;
import com.example.foodshare.repository.DonationRepository;
import com.example.foodshare.repository.ReportRepository;
import com.example.foodshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

//@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {
    
    private final UserRepository userRepository;
    private final DonationRepository donationRepository;
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("=== Checking if demo users need to be initialized ===");
        
        try {
            // Only initialize if database is empty
            long userCount = userRepository.count();
            if (userCount > 0) {
                log.info("Database already contains {} users. Skipping initialization.", userCount);
                return;
            }
            
            log.info("Initializing demo data...");
            
            // Create demo users
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@foodshare.com")
                    .password(passwordEncoder.encode("password123"))
                    .phone("1234567890")
                    .city("Chennai")
                    .role("ROLE_ADMIN")
                    .createdAt(Instant.now())
                    .build();
            
            User donor = User.builder()
                    .name("Donor Demo")
                    .email("donor@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .phone("9876543210")
                    .city("Bengaluru")
                    .role("ROLE_DONOR")
                    .createdAt(Instant.now())
                    .build();
            
            User receiver = User.builder()
                    .name("Receiver Demo")
                    .email("receiver@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .phone("9876554321")
                    .city("Delhi")
                    .role("ROLE_RECEIVER")
                    .createdAt(Instant.now())
                    .build();
            
            admin = userRepository.save(admin);
            donor = userRepository.save(donor);
            receiver = userRepository.save(receiver);
            
            log.info("✓ Created demo users");
            
            // Create sample donations
            Donation donation1 = Donation.builder()
                    .donor(donor)
                    .foodName("Fresh Vegetables")
                    .foodType("VEG")
                    .quantity("5 kg")
                    .description("Fresh organic vegetables from our farm")
                    .expiryDate(LocalDate.now().plusDays(2))
                    .city("Bengaluru")
                    .status("available")
                    .imageUrl("/images/vegetables.jpg")
                    .createdAt(Instant.now().minus(15, ChronoUnit.DAYS))
                    .build();
            
            Donation donation2 = Donation.builder()
                    .donor(donor)
                    .foodName("Cooked Rice")
                    .foodType("VEG")
                    .quantity("10 servings")
                    .description("Freshly cooked rice for evening distribution")
                    .expiryDate(LocalDate.now().plusDays(1))
                    .city("Bengaluru")
                    .status("available")
                    .imageUrl("/images/rice.jpg")
                    .createdAt(Instant.now().minus(12, ChronoUnit.DAYS))
                    .build();
            
            Donation donation3 = Donation.builder()
                    .donor(donor)
                    .foodName("Bread Loaves")
                    .foodType("VEG")
                    .quantity("20 loaves")
                    .description("Fresh bread from bakery surplus")
                    .expiryDate(LocalDate.now().plusDays(3))
                    .city("Bengaluru")
                    .status("available")
                    .imageUrl("/images/bread.jpg")
                    .createdAt(Instant.now().minus(10, ChronoUnit.DAYS))
                    .build();
            
            Donation donation4 = Donation.builder()
                    .donor(donor)
                    .foodName("Mixed Fruits")
                    .foodType("VEG")
                    .quantity("8 kg")
                    .description("Seasonal fruits - apples, bananas, oranges")
                    .expiryDate(LocalDate.now().plusDays(4))
                    .city("Bengaluru")
                    .status("claimed")
                    .imageUrl("/images/fruits.jpg")
                    .createdAt(Instant.now().minus(6, ChronoUnit.DAYS))
                    .build();
            
            Donation donation5 = Donation.builder()
                    .donor(donor)
                    .foodName("Prepared Meals")
                    .foodType("NON-VEG")
                    .quantity("30 meals")
                    .description("Restaurant surplus meals in good condition")
                    .expiryDate(LocalDate.now().plusDays(1))
                    .city("Bengaluru")
                    .status("available")
                    .imageUrl("/images/meals.jpg")
                    .createdAt(Instant.now().minus(3, ChronoUnit.DAYS))
                    .build();
            
            donationRepository.save(donation1);
            donationRepository.save(donation2);
            donationRepository.save(donation3);
            donationRepository.save(donation4);
            donationRepository.save(donation5);
            
            log.info("✓ Created sample donations");
            
            // Create sample reports
            for (int i = 14; i >= 0; i--) {
                Report report = Report.builder()
                        .reportDate(LocalDate.now().minusDays(i))
                        .totalDonations(15 + (i % 10))
                        .totalFoodSavedKg(new BigDecimal(85.5 + (i * 5.2)))
                        .totalReceiversServed(42 + (i * 2))
                        .build();
                reportRepository.save(report);
            }
            
            log.info("✓ Created sample reports");
            log.info("=== Demo data initialization complete ===");
            
        } catch (Exception e) {
            log.error("Error initializing demo data: ", e);
            // Don't throw exception - allow application to start even if data init fails
        }
    }
}
