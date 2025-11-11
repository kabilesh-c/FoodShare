package com.example.foodshare.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "total_donations")
    private int totalDonations;
    @Column(name = "total_food_saved_kg")
    private BigDecimal totalFoodSavedKg;
    @Column(name = "total_receivers_served")
    private int totalReceiversServed;
    @Column(name = "report_date")
    private LocalDate reportDate;
}
