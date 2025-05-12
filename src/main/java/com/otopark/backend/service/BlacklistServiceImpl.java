package com.otopark.backend.service;

import com.otopark.backend.entity.Blacklist;
import com.otopark.backend.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository repo;

    @Override
    @Transactional
    public String add(String plate) {
        if (repo.findByPlate(plate).isPresent()) {
            throw new IllegalArgumentException("Plate is already blacklisted: " + plate);
        }
        Blacklist bl = Blacklist.builder()
                .plate(plate)
                .build();
        repo.save(bl);
        return plate;
    }

    @Override
    @Transactional
    public void remove(String plate) {
        Blacklist bl = repo.findByPlate(plate)
                .orElseThrow(() -> new IllegalArgumentException("Plate not in blacklist: " + plate));
        repo.delete(bl);
    }

    @Override
    public List<String> getAll() {
        return repo.findAll()
                .stream()
                .map(Blacklist::getPlate)
                .collect(Collectors.toList());
    }
}
