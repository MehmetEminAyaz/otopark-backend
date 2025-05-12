package com.otopark.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class ParkResponse {
    private String status;         // ENTRY / EXIT
    private String plate;
    private String vehicleType;    // Yeni
    private Double confidence;     // Mevcut
    private String slot;
    private LocalDateTime time;
    private Double fee;            // EXIT için
}
