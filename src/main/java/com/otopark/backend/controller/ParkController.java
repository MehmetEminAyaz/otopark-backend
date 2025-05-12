package com.otopark.backend.controller;

import com.otopark.backend.dto.ParkResponse;
import com.otopark.backend.service.PlateInfo;
import com.otopark.backend.service.PlateRecognitionService;
import com.otopark.backend.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/park")
@RequiredArgsConstructor
public class ParkController {

    private final PlateRecognitionService plateService;
    private final ParkingService parkingService;

    @Value("${esp32.api.key}")
    private String esp32ApiKey;

    @Operation(
            summary      = "Araç fotoğrafı ile ENTRY/EXIT yapar",
            description  = "multipart/form-data ile gelen resmi Base64’e çevirir, PlateRecognizer ile araç tipini ve confidence’ı alır, ilkse ENTRY, ikincisinde EXIT işlemi yapar"
    )
    @PostMapping(path = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> handlePhoto(
            @RequestHeader("X-API-KEY")
            @Parameter(description = "ESP32 için API anahtarı", required = true)
            String apiKey,

            @RequestPart("image")
            @Parameter(description = "JPEG/PNG formatında araç fotoğrafı", required = true)
            @NotNull MultipartFile imageFile
    ) throws Exception {
        // 1) API-Key kontrolü
        if (!esp32ApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Geçersiz API anahtarı");
        }

        // 2) Resmi Base64’e çevir
        String base64 = java.util.Base64.getEncoder()
                .encodeToString(imageFile.getBytes());

        // 3) PlateRecognizer’dan bilgileri al
        PlateInfo info = plateService.recognizeBase64(base64);
        String plate = info.getPlate();

        // 4) Aktif entry var mı?
        Optional<com.otopark.backend.entity.Vehicle> active =
                parkingService.findActiveVehicle(plate);

        if (active.isPresent()) {
            // → EXIT işlemi
            var result = parkingService.exitVehicle(plate);
            ParkResponse resp = new ParkResponse(
                    "EXIT",
                    plate,
                    info.getVehicleType(),
                    info.getConfidence(),
                    result.getParkingSlot(),
                    result.getExitTime(),
                    result.getFee()
            );
            return ResponseEntity.ok(resp);
        } else {
            // → ENTRY işlemi
            var entry = parkingService.parkVehicle(
                    plate, info.getVehicleType(), Double.valueOf(String.valueOf(info.getConfidence()))
            );
            ParkResponse resp = new ParkResponse(
                    "ENTRY",
                    plate,
                    info.getVehicleType(),
                    info.getConfidence(),
                    entry.getParkingSlot(),
                    entry.getEntryTime(),
                    null
            );
            return ResponseEntity.ok(resp);
        }
    }
}
