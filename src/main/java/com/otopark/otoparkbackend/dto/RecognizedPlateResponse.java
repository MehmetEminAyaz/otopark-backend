package com.otopark.otoparkbackend.dto;

import com.otopark.otoparkbackend.entity.EntryType;

public class RecognizedPlateResponse {

    private String plate;
    private double score;
    private String vehicleType;

    private boolean blacklisted;
    private EntryType status; // ENTRY veya EXIT
    private Long durationMinutes; // varsa, çıkış anında
    private Double fee; // varsa, çıkış anında

    public RecognizedPlateResponse(String plate, double score, String vehicleType,
                                   boolean blacklisted, EntryType status,
                                   Long durationMinutes, Double fee) {
        this.plate = plate;
        this.score = score;
        this.vehicleType = vehicleType;
        this.blacklisted = blacklisted;
        this.status = status;
        this.durationMinutes = durationMinutes;
        this.fee = fee;
    }

    // Getters and Setters

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public EntryType getStatus() {
        return status;
    }

    public void setStatus(EntryType status) {
        this.status = status;
    }

    public Long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
}
