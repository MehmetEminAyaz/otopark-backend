package com.otopark.backend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class PlateInfo {
    private final String plate;
    private final String vehicleType;   // Yeni: Sedan, SUV vs.
    private final Double confidence;
}
