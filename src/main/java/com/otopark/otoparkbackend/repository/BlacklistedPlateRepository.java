package com.otopark.otoparkbackend.repository;

import com.otopark.otoparkbackend.entity.BlacklistedPlate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedPlateRepository extends JpaRepository<BlacklistedPlate, Long> {
    boolean existsByPlate(String plate);
}
