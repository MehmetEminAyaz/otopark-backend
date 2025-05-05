package com.otopark.otoparkbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PlateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private EntryType type;

    private boolean blacklisted;

    private double confidence;

    private String vehicleType;

    private Long durationMinutes;

    private Double fee;

    // Getters & Setters
}
