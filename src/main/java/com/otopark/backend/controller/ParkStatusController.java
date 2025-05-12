package com.otopark.backend.controller;

import com.otopark.backend.dto.OccupancyResponse;
import com.otopark.backend.repository.ParkingSlotRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/park")
public class ParkStatusController {

    private final ParkingSlotRepository slotRepo;

    public ParkStatusController(ParkingSlotRepository slotRepo) {
        this.slotRepo = slotRepo;
    }

    @Operation(summary = "Otopark doluluk durumunu döner")
    @GetMapping("/status")
    public ResponseEntity<OccupancyResponse> getStatus() {
        var allSlots = slotRepo.findAll();
        int total = allSlots.size();
        var freeSlots = allSlots.stream()
                .filter(s -> !s.isOccupied())
                .map(s -> s.getSlotId())
                .collect(Collectors.toList());
        int free  = freeSlots.size();
        int occ   = total - free;
        var resp = new OccupancyResponse(total, occ, free, freeSlots);
        return ResponseEntity.ok(resp);
    }
}
