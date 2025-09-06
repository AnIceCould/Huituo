package com.mining.dispatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * ActiveMQ 消息生产者服务
 * 负责将数据发布到指定的 Topic
 */
@Service
public class ActiveMQProducerService {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送消息到指定主题
     * @param topic 主题名称
     * @param message 消息内容 (JSON字符串)
     */
    public void sendMessage(String topic, String message) {
        System.out.println("发送消息到主题: " + topic);
        jmsTemplate.convertAndSend(topic, message);
    }
}
