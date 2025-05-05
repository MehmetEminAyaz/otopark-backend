package com.otopark.otoparkbackend.service;

import com.otopark.otoparkbackend.entity.BlacklistedPlate;
import com.otopark.otoparkbackend.repository.BlacklistedPlateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistedPlateService {

    private final BlacklistedPlateRepository blacklistedPlateRepository;

    public void addToBlacklist(String plate) {
        if (!blacklistedPlateRepository.existsByPlate(plate)) {
            BlacklistedPlate newEntry = new BlacklistedPlate();
            newEntry.setPlate(plate);
            blacklistedPlateRepository.save(newEntry);
        }
    }
}
