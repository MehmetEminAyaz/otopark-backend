package com.otopark.backend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class ExitResult {
    private final String parkingSlot;
    private final LocalDateTime exitTime;
    private final double fee;
}
