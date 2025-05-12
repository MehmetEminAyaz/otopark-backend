package com.otopark.backend.repository;

import com.otopark.backend.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByPlate(String plate);
}
