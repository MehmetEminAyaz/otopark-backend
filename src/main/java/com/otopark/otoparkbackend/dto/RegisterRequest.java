package com.otopark.otoparkbackend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
