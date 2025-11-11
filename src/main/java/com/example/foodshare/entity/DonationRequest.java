package com.example.foodshare.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "donation_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationRequest {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private User receiver;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donation_id")
    private Donation donation;
    private String status; // pending, approved, completed
    @Builder.Default
    @Column(name = "requested_at")
    private Instant requestedAt = Instant.now();
}
