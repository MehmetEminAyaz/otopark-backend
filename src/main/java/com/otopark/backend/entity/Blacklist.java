package com.otopark.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blacklist")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plate;
}
