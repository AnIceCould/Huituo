package com.mining.dispatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mining.dispatch.model.VehicleData;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * 这是一个消息消费者服务，用于解析无人矿卡数据并打印所有字段。
 */
@Service
public class DataProcessingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 监听无人矿卡数据主题，将接收到的消息解析为 VehicleData 对象并打印所有字段。
     *
     * @param message 接收到的消息，通常是 JSON 字符串
     */
    @JmsListener(destination = "telemetry.raw.autonomous")
    public void processAutonomousData(String message) {
        System.out.println("数据处理服务：接收到无人矿卡消息。");
        try {
            // 1. JSON 解析: 将原始 JSON 字符串解析成 VehicleData 对象
            VehicleData data = objectMapper.readValue(message, VehicleData.class);

            // 2. 打印所有解析出的字段
            System.out.println("成功解析所有字段：");
            System.out.println("  - vehicleId: " + data.getVehicleId());
            System.out.println("  - latitude: " + data.getLatitude());
            System.out.println("  - longitude: " + data.getLongitude());
            System.out.println("  - speed: " + data.getSpeed());
            System.out.println("  - fuelLevel: " + data.getFuelLevel());
            System.out.println("  - timestamp: " + data.getTimestamp());
            System.out.println("  - alarm: " + data.isAlarm());

        } catch (Exception e) {
            System.err.println("JSON 解析失败: " + e.getMessage());
        }
    }
}