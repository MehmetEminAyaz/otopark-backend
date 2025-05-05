package com.otopark.otoparkbackend.service;

import com.otopark.otoparkbackend.entity.*;
import com.otopark.otoparkbackend.repository.*;
import com.otopark.otoparkbackend.dto.RecognizedPlateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlateRecognitionService {

    @Autowired
    private PlateRecognizerService plateRecognizerService;

    @Autowired
    private PlateLogRepository plateLogRepository;

    @Autowired
    private BlacklistedPlateRepository blacklistedPlateRepository;

    public RecognizedPlateResponse recognizeAndProcessPlate(MultipartFile image) {
        // 1. OCR'dan sonucu al
        RecognizedPlateResponse ocrResult = plateRecognizerService.recognizePlate(image);
        String plate = ocrResult.getPlate();
        double score = ocrResult.getScore();
        String vehicleType = ocrResult.getVehicleType();

        // 2. Kara liste kontrolü
        boolean isBlacklisted = blacklistedPlateRepository.existsByPlate(plate);

        // 3. Son işlem var mı kontrol et (giriş yapılmış mı?)
        Optional<PlateLog> lastLogOpt = plateLogRepository.findTopByPlateOrderByTimestampDesc(plate);
        EntryType newType = EntryType.ENTRY;
        Long duration = null;
        Double fee = null;

        if (lastLogOpt.isPresent() && lastLogOpt.get().getType() == EntryType.ENTRY) {
            newType = EntryType.EXIT;
            LocalDateTime entryTime = lastLogOpt.get().getTimestamp();
            duration = Duration.between(entryTime, LocalDateTime.now()).toMinutes();
            fee = calculateFee(duration);
        }

        // 4. Yeni log kaydı oluştur
        PlateLog newLog = new PlateLog();
        newLog.setPlate(plate);
        newLog.setTimestamp(LocalDateTime.now());
        newLog.setType(newType);
        newLog.setBlacklisted(isBlacklisted);
        newLog.setConfidence(score);
        newLog.setVehicleType(vehicleType);
        newLog.setDurationMinutes(duration);
        newLog.setFee(fee);

        plateLogRepository.save(newLog);

        // 5. Kullanıcıya dönecek veri
        return new RecognizedPlateResponse(
                plate, score, vehicleType,
                isBlacklisted, newType, duration, fee
        );
    }

    private double calculateFee(Long minutes) {
        if (minutes == null) return 0.0;
        return Math.ceil(minutes / 30.0) * 5.0; //  her 30 dakikaya 5 TL
    }
}
