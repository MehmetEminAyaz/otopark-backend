package com.otopark.otoparkbackend.repository;

import com.otopark.otoparkbackend.entity.PlateLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlateLogRepository extends JpaRepository<PlateLog, Long> {
    Optional<PlateLog> findTopByPlateOrderByTimestampDesc(String plate);
}
