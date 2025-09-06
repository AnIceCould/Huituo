package com.mining.dispatch.repository;

import com.mining.dispatch.entity.TelemetryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryDataRepository extends JpaRepository<TelemetryData, Long> {
}