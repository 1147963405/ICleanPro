package com.iclean.pt.sbgl.dao;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    /**
     * 发送信息到MQTT服务器
     *
     * @param topic 主题
     * @param message 消息主体
     */
    void publishMqttMessageWithTopic(String message, @Header(MqttHeaders.TOPIC) String topic);

    /**
     * 发送信息到MQTT服务器
     *
     * @param topic 主题
     * @param qos 对消息处理的几种机制。<br> 0 表示的是订阅者没收到消息不会再次发送，消息会丢失。<br>
     * 1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。<br>
     * 2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param message 消息主体
     */
    void publishMqttMessageWithTopic(String message, @Header(MqttHeaders.TOPIC) String topic,@Header(MqttHeaders.QOS) int qos);
}


