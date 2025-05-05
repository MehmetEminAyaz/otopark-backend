package com.otopark.otoparkbackend.controller;

import com.otopark.otoparkbackend.dto.RecognizedPlateResponse;
import com.otopark.otoparkbackend.service.PlateRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/plate")
public class PlateRecognitionController {

    @Autowired
    private PlateRecognitionService plateRecognitionService;

    @PostMapping(value = "/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecognizedPlateResponse> recognizePlate(
            @RequestParam("image") MultipartFile image
    ) {
        return ResponseEntity.ok(plateRecognitionService.recognizeAndProcessPlate(image));
    }
}
