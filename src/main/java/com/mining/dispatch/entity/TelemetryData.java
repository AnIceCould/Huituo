package com.mining.dispatch.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "telemetry_data")
public class TelemetryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(columnDefinition = "TEXT")
    private String rawData;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}