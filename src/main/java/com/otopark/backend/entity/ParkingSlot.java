package com.otopark.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_slots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingSlot {
    @Id
    @Column(name = "slot_id", length = 3)
    private String slotId;      // e.g. "A1", "B10"

    @Column(nullable = false)
    private boolean occupied;
}
