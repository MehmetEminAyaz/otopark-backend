package com.otopark.otoparkbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otopark.otoparkbackend.dto.RecognizedPlateResponse;
import com.otopark.otoparkbackend.entity.EntryType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PlateRecognizerService {

    @Value("${platerecognizer.api.url}")
    private String apiUrl;

    @Value("${platerecognizer.api.key}")
    private String apiKey;

    public RecognizedPlateResponse recognizePlate(MultipartFile imageFile) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Authorization", "Token " + apiKey); // 🔐 API Key

            ByteArrayResource fileAsResource = new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("upload", fileAsResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode firstResult = root.path("results").get(0);

            String plate = firstResult.path("plate").asText();
            double score = firstResult.path("score").asDouble();
            String vehicleType = firstResult.path("vehicle").path("type").asText();

            // Sadece OCR sonucunu dönüyoruz, diğer alanlar null/default
            return new RecognizedPlateResponse(
                    plate,
                    score,
                    vehicleType,
                    false,
                    null,
                    null,
                    null
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new RecognizedPlateResponse(
                    "Hata",
                    0.0,
                    "Bilinmiyor",
                    false,
                    null,
                    null,
                    null
            );
        }
    }
}
