package com.otopark.backend.service;

import com.otopark.backend.entity.ParkingSlot;
import com.otopark.backend.entity.Transaction;
import com.otopark.backend.entity.Vehicle;
import com.otopark.backend.repository.ParkingSlotRepository;
import com.otopark.backend.repository.TransactionRepository;
import com.otopark.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final VehicleRepository vehicleRepo;
    private final ParkingSlotRepository slotRepo;
    private final TransactionRepository transactionRepo;
    private final OccupancyPublisher occupancyPublisher;

    @Override
    public Optional<Vehicle> findActiveVehicle(String plate) {
        return vehicleRepo.findByPlateAndExitTimeIsNull(plate);
    }

    @Override
    @Transactional
    public Vehicle parkVehicle(String plate, String vehicleType, Double confidence) {
        ParkingSlot slot = slotRepo.findByOccupiedFalse()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No free slots"));
        slot.setOccupied(true);
        slotRepo.save(slot);

        Vehicle v = Vehicle.builder()
                .plate(plate)
                .model(vehicleType)
                .confidence(confidence)
                .entryTime(LocalDateTime.now())
                .parkingSlot(slot.getSlotId())
                .build();
        Vehicle saved = vehicleRepo.save(v);

        occupancyPublisher.publish();
        return saved;
    }

    @Override
    @Transactional
    public ExitResult exitVehicle(String plate) {
        Vehicle v = vehicleRepo.findByPlateAndExitTimeIsNull(plate)
                .orElseThrow(() -> new IllegalArgumentException("No active entry for this plate"));

        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(v.getEntryTime(), exitTime);
        double fee = 20.0 + Math.max(0, (minutes - 30 + 29) / 30) * 20.0;

        Transaction tx = Transaction.builder()
                .plate(plate)
                .entryTime(v.getEntryTime())
                .exitTime(exitTime)
                .durationMinutes(minutes)
                .fee(fee)
                .build();
        transactionRepo.save(tx);

        ParkingSlot slot = slotRepo.findById(v.getParkingSlot()).orElseThrow();
        slot.setOccupied(false);
        slotRepo.save(slot);

        v.setExitTime(exitTime);
        vehicleRepo.save(v);

        occupancyPublisher.publish();
        return new ExitResult(slot.getSlotId(), exitTime, fee);
    }
}
