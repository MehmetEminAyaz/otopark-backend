package com.otopark.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plate;

    private String model;
    private String color;

    @Column(nullable = false)
    private LocalDateTime entryTime;
    // yeni: confidence skoru
    @Column(name = "confidence")
    private Double confidence;
    private LocalDateTime exitTime;

    @Column(nullable = false)
    private String parkingSlot;
}
