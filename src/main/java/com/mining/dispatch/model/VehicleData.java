package com.mining.dispatch.model;

import lombok.Data;
import java.io.Serializable;
import java.util.UUID;

/**
 * 车辆通用数据模型
 * 统一两种车辆类型的数据格式，方便发送到消息队列
 */
@Data
public class VehicleData implements Serializable {
    private String vehicleId;
    private double latitude;
    private double longitude;
    private double speed;
    private double fuelLevel;
    private boolean isAlarm;
    private long timestamp;
}