package com.otopark.otoparkbackend.controller;

import com.otopark.otoparkbackend.dto.BlacklistRequest;
import com.otopark.otoparkbackend.service.BlacklistedPlateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blacklist")
@RequiredArgsConstructor
public class BlacklistedPlateController {

    private final BlacklistedPlateService blacklistedPlateService;

    @PostMapping("/add")
    public ResponseEntity<String> addToBlacklist(@RequestBody BlacklistRequest request) {
        blacklistedPlateService.addToBlacklist(request.getPlate());
        return ResponseEntity.ok("Plaka kara listeye eklendi.");
    }
}
