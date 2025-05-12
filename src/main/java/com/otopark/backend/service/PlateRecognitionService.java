package com.otopark.backend.service;

public interface PlateRecognitionService {
    //PlateInfo recognize(String imageUrl);
    PlateInfo recognizeBase64(String base64Image);
}
