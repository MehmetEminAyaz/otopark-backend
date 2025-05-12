package com.otopark.backend.controller;

import com.otopark.backend.dto.AuthRequest;
import com.otopark.backend.dto.AuthResponse;
import com.otopark.backend.entity.Role;
import com.otopark.backend.entity.User;
import com.otopark.backend.repository.UserRepository;
import com.otopark.backend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // src/main/java/com/otopark/backend/controller/AuthController.java

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequest req) {
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                // USER yerine ADMIN ata
                .role(Role.ROLE_ADMIN)
                .build();
        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(), req.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Geçersiz kimlik bilgileri");
        }

        String token = jwtUtil.generateToken(req.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
