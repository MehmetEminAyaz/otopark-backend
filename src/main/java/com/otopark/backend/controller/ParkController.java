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
import org.springframework.messaging.simp.SimpMessagingTemplate;        // ← ekledik
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/park")
@RequiredArgsConstructor
public class ParkController {

    private final PlateRecognitionService plateService;
    private final ParkingService parkingService;
    private final SimpMessagingTemplate messagingTemplate;             // ← ekledik

    @Value("${esp32.api.key}")
    private String esp32ApiKey;

    @Operation(
            summary     = "Araç fotoğrafı ile ENTRY/EXIT yapar",
            description = "multipart/form-data ile gelen resmi Base64’e çevirir, PlateRecognizer ile araç tipini ve confidence’ı alır…"
    )
    @PostMapping(path = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ParkResponse> handlePhoto(
            @RequestHeader("X-API-KEY")
            @Parameter(description = "ESP32 için API anahtarı", required = true)
            String apiKey,

            @RequestPart("image")
            @Parameter(description = "JPEG/PNG formatında araç fotoğrafı", required = true)
            @NotNull MultipartFile imageFile
    ) throws Exception {
        if (!esp32ApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        String base64 = java.util.Base64.getEncoder()
                .encodeToString(imageFile.getBytes());

        PlateInfo info = plateService.recognizeBase64(base64);
        String plate   = info.getPlate();

        ParkResponse resp;
        Optional<?> active = parkingService.findActiveVehicle(plate);
        if (active.isPresent()) {
            var result = parkingService.exitVehicle(plate);
            resp = new ParkResponse(
                    "EXIT", plate, info.getVehicleType(),
                    info.getConfidence(),
                    result.getParkingSlot(),
                    result.getExitTime(),
                    result.getFee()
            );
        } else {
            var entry = parkingService.parkVehicle(
                    plate, info.getVehicleType(),
                    info.getConfidence().doubleValue()
            );
            resp = new ParkResponse(
                    "ENTRY", plate, info.getVehicleType(),
                    info.getConfidence(),
                    entry.getParkingSlot(),
                    entry.getEntryTime(),
                    null
            );
        }

        // → REST cevabından önce aynı event'i STOMP üzerinden yayınlayalım
        messagingTemplate.convertAndSend("/topic/occupancy", resp);

        return ResponseEntity.ok(resp);
    }
}
