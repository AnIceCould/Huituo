package com.mining.dispatch.client;

import com.mining.dispatch.model.VehicleData;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 无人矿卡数据客户端
 * 模拟从 Netty Server 获取 TCP 实时数据流
 * (在实际应用中，这里会是一个 Netty 客户端或一个直接处理 TCP 数据的服务)
 */
@Component
public class NettyClientSimulator {

    private final Random random = new Random();

    /**
     * 模拟从 Netty TCP 流中获取实时数据
     * @return 车辆数据列表
     */
    public List<VehicleData> getRealTimeData() {
        System.out.println("从 Netty Server 模拟接收无人矿卡数据...");
        // 模拟8台无人矿卡的数据
        List<VehicleData> dataList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            VehicleData data = new VehicleData();
            data.setVehicleId("Autonomous-" + i);
            data.setLatitude(22.0 + random.nextDouble());
            data.setLongitude(113.0 + random.nextDouble());
            data.setSpeed(random.nextDouble() * 50);
            data.setFuelLevel(random.nextDouble() * 100);
            // 模拟随机报警
            data.setAlarm(random.nextBoolean());
            data.setTimestamp(System.currentTimeMillis());
            dataList.add(data);
        }
        return dataList;
    }
}
