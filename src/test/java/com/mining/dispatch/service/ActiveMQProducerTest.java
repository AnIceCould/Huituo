package com.mining.dispatch.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * ActiveMQProducerService 的单元测试类。
 * 使用 Mockito 模拟 JmsTemplate，以验证消息发送逻辑。
 */
@ExtendWith(MockitoExtension.class)
class ActiveMQProducerServiceTest {

    // 自动创建 ActiveMQProducerService 实例，并注入所有 @Mock 依赖
    @InjectMocks
    private ActiveMQProducerService activeMQProducerService;

    // 创建 JmsTemplate 的模拟对象
    @Mock
    private JmsTemplate mockJmsTemplate;

    @Test
    void testSendMessage() {
        // 定义测试数据
        String topic = "telemetry.raw.autonomous";
        String message = "{\"vehicleId\":\"test-001\",\"data\":\"test-data\"}";

        // 1. 设置模拟行为：当调用 mockJmsTemplate.convertAndSend 时，不执行任何操作
        doNothing().when(mockJmsTemplate).convertAndSend(topic, message);

        // 2. 调用要测试的方法
        assertDoesNotThrow(() -> activeMQProducerService.sendMessage(topic, message));

        // 3. 验证行为：确认 convertAndSend 方法被且仅被调用了一次，且参数正确
        verify(mockJmsTemplate, times(1)).convertAndSend(topic, message);
    }
}
