package com.otopark.backend.controller;

import com.otopark.backend.entity.Transaction;
import com.otopark.backend.entity.Vehicle;
import com.otopark.backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Tüm araç geçiş kayıtlarını döner",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> listVehicles() {
        return ResponseEntity.ok(adminService.getAllVehicles());
    }

    @Operation(summary = "Tüm işlem (ücretlendirme) kayıtlarını döner",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> listTransactions() {
        return ResponseEntity.ok(adminService.getAllTransactions());
    }
}
