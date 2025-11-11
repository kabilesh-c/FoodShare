package com.example.foodshare.entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "donations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @NotBlank(message = "Food name is required")
    @Size(min = 2, max = 100, message = "Food name must be between 2 and 100 characters")
    @Column(name = "food_name", nullable = false)
    private String foodName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Quantity is required")
    @Size(min = 1, max = 50, message = "Quantity must be between 1 and 50 characters")
    private String quantity;
    
    @NotBlank(message = "Food type is required")
    @Column(name = "food_type")
    private String foodType;
    
    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;
    
    private String status; // available, claimed, expired
    
    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}
