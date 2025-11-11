package com.example.foodshare.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @Builder.Default
    @Column(name = "matched_at")
    private Instant matchedAt = Instant.now();
}
