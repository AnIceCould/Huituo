package com.mining.dispatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * ActiveMQ 消息消费者服务
 * 监听并处理来自消息队列的消息
 */
@Service
public class ActiveMQConsumerService {

    private final TelemetryDataService telemetryDataService;

    @Autowired
    public ActiveMQConsumerService(TelemetryDataService telemetryDataService) {
        this.telemetryDataService = telemetryDataService;
    }

    /**
     * 监听无人矿卡数据主题
     * @param message 接收到的消息，通常是 JSON 字符串
     */
    @JmsListener(destination = "telemetry.raw.autonomous")
    public void receiveAutonomousData(String message) {
        System.out.println("成功接收到来自无人矿卡的消息：");
        System.out.println(message);
        telemetryDataService.saveTelemetryData("无人矿卡", message);
    }

    /**
     * 监听有人矿卡数据主题
     * @param message 接收到的消息，通常是 JSON 字符串
     */
    @JmsListener(destination = "telemetry.raw.manned")
    public void receiveMannedData(String message) {
        System.out.println("成功接收到来自有人矿卡的消息：");
        System.out.println(message);
        telemetryDataService.saveTelemetryData("有人矿卡", message);
    }
}