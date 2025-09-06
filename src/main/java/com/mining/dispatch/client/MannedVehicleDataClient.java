package com.mining.dispatch.client;

import com.mining.dispatch.model.VehicleData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.List;

/**
 * 工程设备数据客户端
 * 模拟通过 RESTful API 获取博云视控的实时数据
 */
@Component
public class MannedVehicleDataClient {

    @Value("${manned.data.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public MannedVehicleDataClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 从 RESTful API 获取工程设备实时数据
     * @return 车辆数据列表
     */
    public List<VehicleData> getRealTimeData() {
        System.out.println("从博云视控 API 拉取数据...");
        try {
            // 注意：这里需要根据实际的 API 返回类型调整
            ResponseEntity<List<VehicleData>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<VehicleData>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("从博云视控 API 获取数据失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
