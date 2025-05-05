package com.otopark.otoparkbackend.controller;

import com.otopark.otoparkbackend.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @Value("${photo.upload.apikey}")
    private String validApiKey;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("image") MultipartFile image,
            @RequestHeader("apikey") String apiKey) {

        System.out.println("Received API key: " + apiKey);
        System.out.println("Expected API key: " + validApiKey);

        if (!validApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid API Key");
        }

        String fileName = photoService.saveImage(image);
        return ResponseEntity.ok("Uploaded successfully: " + fileName);
    }


}
