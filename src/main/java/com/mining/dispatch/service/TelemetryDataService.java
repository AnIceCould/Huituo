package com.mining.dispatch.service;

import com.mining.dispatch.entity.TelemetryData;
import com.mining.dispatch.repository.TelemetryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TelemetryDataService {

    private final TelemetryDataRepository telemetryDataRepository;

    @Autowired
    public TelemetryDataService(TelemetryDataRepository telemetryDataRepository) {
        this.telemetryDataRepository = telemetryDataRepository;
    }

    /**
     * 保存遥测数据到数据库
     * @param vehicleType 车辆类型（无人或有人）
     * @param rawData 原始消息内容 (JSON字符串)
     */
    public void saveTelemetryData(String vehicleType, String rawData) {
        try {
            TelemetryData telemetryData = new TelemetryData();
            telemetryData.setVehicleType(vehicleType);
            telemetryData.setRawData(rawData);
            telemetryData.setCreatedTime(LocalDateTime.now());

            telemetryDataRepository.save(telemetryData);
            System.out.println("数据已成功保存到数据库。");
        } catch (Exception e) {
            System.err.println("保存数据到数据库时出错: " + e.getMessage());
        }
    }
}