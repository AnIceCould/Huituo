package com.mining.dispatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mining.dispatch.model.VehicleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个消息消费者服务，用于解析无人矿卡数据并使用 Lua 脚本将其原子性地缓存到 Redis。
 */
@Service
public class DataProcessingService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("classpath:lua/update_vehicle_data.lua")
    private Resource scriptSource;

    private RedisScript<String> redisScript;

    @Autowired
    public DataProcessingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 在 bean 初始化后加载 Lua 脚本
     */
    @PostConstruct
    public void init() throws IOException {
        String scriptContent = StreamUtils.copyToString(scriptSource.getInputStream(), Charset.defaultCharset());
        this.redisScript = new DefaultRedisScript<>(scriptContent, String.class);
    }

    /**
     * 监听无人矿卡数据主题，将接收到的消息解析为 VehicleData 对象，并使用 Lua 脚本原子性地缓存到 Redis。
     *
     * @param message 接收到的消息，通常是 JSON 字符串
     */
    @JmsListener(destination = "telemetry.raw.autonomous")
    public void processAutonomousData(String message) {
        System.out.println("数据处理服务：接收到无人矿卡消息。");
        System.out.println("接收到的原始消息: " + message); // 添加这一行
        try {
            // 1. JSON 解析: 将原始 JSON 字符串解析成 VehicleData 对象
            VehicleData data = objectMapper.readValue(message, VehicleData.class);

            // 2. 准备 KEYS 和 ARGV 参数
            List<String> keys = Arrays.asList("vehicle:locations", "vehicle:speeds", "vehicle:");

            List<Object> args = new ArrayList<>();
            args.add(data.getVehicleId());
            args.add(String.valueOf(data.getLatitude()));   // double -> String
            args.add(String.valueOf(data.getLongitude()));  // double -> String
            args.add(String.valueOf(data.getSpeed()));      // double -> String
            args.add(String.valueOf(data.getFuelLevel()));  // double -> String
            args.add(String.valueOf(data.isAlarm()));       // boolean -> String
            args.add(String.valueOf(data.getTimestamp()));  // long -> String


            // 3. 执行 Lua 脚本
            String result = stringRedisTemplate.execute(redisScript, keys, args.toArray());


            System.out.println("成功使用 Lua 脚本原子性地写入数据。");
            System.out.println("  - Redis 脚本执行结果: " + result);

        } catch (Exception e) {
            System.err.println("处理消息时出错: " + e.getMessage());
        }
    }
}