// src/main/java/com/otopark/backend/dto/PhotoRequest.java
package com.otopark.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PhotoRequest {
    @NotBlank
    private String imageUrl;
}
