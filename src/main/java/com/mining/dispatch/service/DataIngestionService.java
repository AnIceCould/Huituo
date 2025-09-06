package com.mining.dispatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mining.dispatch.client.MannedVehicleDataClient;
import com.mining.dispatch.client.NettyClientSimulator;
import com.mining.dispatch.model.VehicleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DataIngestionService {

    @Autowired
    private ActiveMQProducerService activeMQProducerService;

    // 假设你有专门的服务来获取无人矿卡和工程设备数据
    @Autowired
    private NettyClientSimulator autonomousTruckDataClient;
    @Autowired
    private MannedVehicleDataClient mannedVehicleDataClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 定时采集无人矿卡数据并发布
     * 假设每5秒执行一次，你需要根据实际需求调整 cron 表达式
     */
    @Scheduled(fixedRate = 5000)
    public void ingestAutonomousTruckData() {
        // 在实际生产环境中，请使用专业的日志框架，例如 SLF4J
        System.out.println("--- 正在拉取无人矿卡数据...");
        try {
            List<VehicleData> dataList = autonomousTruckDataClient.getRealTimeData();
            if (dataList != null && !dataList.isEmpty()) {
                String topic = "telemetry.raw.autonomous";
                for (VehicleData data : dataList) {
                    String jsonMessage = objectMapper.writeValueAsString(data);
                    activeMQProducerService.sendMessage(topic, jsonMessage);
                }
            } else {
                System.out.println("--- 未接收到无人矿卡数据。");
            }
        } catch (Exception e) {
            System.err.println("--- 采集无人矿卡数据时出错: " + e.getMessage());
        }
        System.out.println("--- 无人矿卡数据拉取结束。");
    }

    /**
     * 定时采集工程设备数据并发布
     * 假设每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void ingestMannedVehicleData() {
        // 在实际生产环境中，请使用专业的日志框架，例如 SLF4J
        System.out.println("--- 正在拉取工程设备数据...");
        try {
            List<VehicleData> dataList = mannedVehicleDataClient.getRealTimeData();
            if (dataList != null && !dataList.isEmpty()) {
                String topic = "telemetry.raw.manned";
                for (VehicleData data : dataList) {
                    String jsonMessage = objectMapper.writeValueAsString(data);
                    activeMQProducerService.sendMessage(topic, jsonMessage);
                }
            } else {
                System.out.println("--- 未接收到工程设备数据。");
            }
        } catch (Exception e) {
            System.err.println("--- 采集工程设备数据时出错: " + e.getMessage());
        }
        System.out.println("--- 工程设备数据拉取结束。");
    }
}
