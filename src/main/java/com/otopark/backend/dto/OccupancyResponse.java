package com.otopark.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class OccupancyResponse {
    private final int totalSlots;
    private final int occupiedCount;
    private final int freeCount;
    private final List<String> freeSlots;
}
