package com.otopark.backend.service;

import com.otopark.backend.dto.OccupancyResponse;
import com.otopark.backend.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OccupancyPublisher {

    private final ParkingSlotRepository slotRepo;
    private final SimpMessagingTemplate messaging;

    public void publish() {
        var all = slotRepo.findAll();
        int total = all.size();
        List<String> free = all.stream()
                .filter(s -> !s.isOccupied())
                .map(s -> s.getSlotId())
                .collect(Collectors.toList());
        var resp = new OccupancyResponse(total, total - free.size(), free.size(), free);
        messaging.convertAndSend("/topic/occupancy", resp);
    }
}
