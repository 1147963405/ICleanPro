package com.iclean.pt.common.config.Spring;

import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.utils.Constants;
import com.iclean.pt.utils.RedisUtil;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


@Configuration
public class SpringConfig {

    static RedisUtil redisUtil;

    /*****
     * 创建MqttPahoClientFactory，设置MQTT Broker连接属性，如果使用SSL验证，也在这里设置。
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{Constants.Mqtt.SERVER_URL.getValue()});
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(false);
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置自动重连, 其它具体参数可以查看MqttConnectOptions
        options.setAutomaticReconnect(true);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(Constants.Mqtt.CONSUMER_CLIENTID.getValue(),
                mqttClientFactory(), Constants.Mqtt.TOPIC_SUB_ROBOT_MSG.getValue(),
                          Constants.Mqtt.TOPIC_SUB_ROBOT_MSGEX.getValue(),
                          Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue());
//        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(Constants.Mqtt.PRODUCER_CLIENTID.getValue(), mqttClientFactory());
        messageHandler.setAsync(false);
        String topic=Constants.Mqtt.TOPIC_SUB_ROBOT_MSG.getValue()+","+
        Constants.Mqtt.TOPIC_SUB_ROBOT_MSGEX.getValue()+","+
                Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue();
        messageHandler.setDefaultTopic(topic);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }


    @Bean
    //ServiceActivator注解表明当前方法用于处理MQTT消息，inputChannel参数指定了用于接收消息信息的channel。
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public static MessageHandler handler() {
        return message -> {
            String payload = message.getPayload().toString();
            String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
            // 根据topic分别进行消息处理。
            if (topic.contains("message")) {
                JSONObject parseObject = JSONObject.parseObject(payload);
                redisUtil.hset("message", String.valueOf(parseObject.get("device_id")),parseObject);
            } else if (topic.contains("messageEx")) {
                JSONObject parseObject = JSONObject.parseObject(payload);
                redisUtil.hset("messageEx", String.valueOf(parseObject.get("device_id")),parseObject);
            } else if (topic.equals(Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue())){
                JSONObject parseObject = JSONObject.parseObject(payload);
                if(parseObject!=null){
                    redisUtil.hset(topic, String.valueOf(parseObject.get("device_id")),parseObject);
                }
            }
        };
    }
}
