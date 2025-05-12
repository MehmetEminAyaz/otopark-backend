package com.otopark.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PlateRecognitionServiceImpl implements PlateRecognitionService {

    private final WebClient.Builder webClientBuilder;

    @Value("${platerecognizer.api.url}")
    private String apiUrl;

    @Value("${platerecognizer.api.key}")
    private String apiKey;

    @Override
    public PlateInfo recognizeBase64(String base64Image) {
        String body = "{\"upload\":\"data:image/jpeg;base64," + base64Image + "\"}";

        JsonNode resp = webClientBuilder.build()
                .post()
                .uri(apiUrl)
                .headers(h -> h.set("Authorization", "Token " + apiKey))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        JsonNode results = resp.get("results");
        if (results == null || !results.isArray() || results.isEmpty()) {
            throw new RuntimeException("Plate not recognized");
        }
        JsonNode best = results.get(0);

        String plate = best.path("plate").asText();
        Double confidence = best.path("score").asDouble();

        // Ücretsiz planda dönebilen araç tipi
        JsonNode vehicle = best.path("vehicle");
        String type = vehicle.path("type").asText(null);

        return new PlateInfo(plate, type, confidence);
    }
}
