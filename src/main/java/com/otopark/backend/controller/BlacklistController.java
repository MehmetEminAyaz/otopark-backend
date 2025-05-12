package com.otopark.backend.controller;

import com.otopark.backend.service.BlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blacklist")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService service;

    @Operation(summary = "Tüm kara liste plakalarını döner",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<String>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Yeni plaka ekler",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<String> add(@RequestBody AddRequest req) {
        String plate = service.add(req.getPlate());
        return ResponseEntity.ok(plate);
    }

    @Operation(summary = "Kara listeden plaka siler",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{plate}")
    public ResponseEntity<Void> remove(@PathVariable("plate") String plate) {
        service.remove(plate);
        return ResponseEntity.noContent().build();
    }

    @Getter @AllArgsConstructor
    public static class AddRequest {
        @NotBlank
        private String plate;
    }
}
