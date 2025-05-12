package com.otopark.backend.service;

import com.otopark.backend.entity.Vehicle;
import java.util.Optional;

public interface ParkingService {
    Optional<Vehicle> findActiveVehicle(String plate);
    Vehicle parkVehicle(String plate, String vehicleType, Double confidence);
    ExitResult exitVehicle(String plate);
}
