package com.otopark.otoparkbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BlacklistedPlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String plate;

    // Getter & Setter
}
