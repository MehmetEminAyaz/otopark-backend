package com.otopark.backend.repository;

import com.otopark.backend.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, String> {
    List<ParkingSlot> findByOccupiedFalse();
}
