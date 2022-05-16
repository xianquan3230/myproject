package com.lcoil.mq.utils;

import com.aliyun.openservices.ons.api.*;
import com.lcoil.mq.config.RocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Classname MQUtil
 * @Description MQUtil
 * @Date 2022/5/15 7:46 PM
 * @Created by l-coil
 */

@Component
@Slf4j
public class MQUtil {

    @Autowired
    private RocketMQConfig rocketMQConfig;

    /**
     * 发送普通消息
     *
     * @param content 内容
     * @param tag     可理解为Gmail中的标签,对消息进行再归类,方便Consumer指定过滤条件在消息队列
     */
    public void sendMessage(String content, String tag) {
        Message message = new Message();
        message.setBody(content.getBytes());
        message.setTopic(RocketMQConfig.TOPIC);
        message.setTag(tag);
        this.sendCustomerMessage(message);
    }

    /**
     * 发送普通消息
     *
     * @param content
     * @param tag
     * @param topic
     */
    public void sendMessage(String content, String topic, String tag) {
        Message message = new Message();
        message.setBody(content.getBytes());
        message.setTopic(topic);
        message.setTag(tag);
        this.sendCustomerMessage(message);
    }

    /**
     * 发送定时任务
     *
     * @param content   内容
     * @param tag       标签
     * @param delayTime 定时时间
     */
    public void sendDelayMessage(String content, String tag, long delayTime) {
        Message message = new Message();
        message.setBody(content.getBytes());
        message.setTopic(RocketMQConfig.TOPIC);
        message.setTag(tag);
        /**
         * 单位毫秒（ms）
         * 在指定时间戳（当前时间之后）进行投递
         * 例如 2016-03-07 16:21:00 投递
         * 如果被设置成当前时间戳之前的某个时刻，消息将立刻投递给消费者
         */
        message.setStartDeliverTime(System.currentTimeMillis() + delayTime);
        this.sendCustomerMessage(message);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    private void sendCustomerMessage(Message message) {
        Properties properties = rocketMQConfig.getProperties();
        Producer producer = ONSFactory.createProducer(properties);
        //在发送消息前，必须调用start方法来启动Producer,只需调用一次即可
        producer.start();
        try {
            SendResult sendResult = producer.send(message);
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
                log.info("消息发送成功：messageID：" + sendResult.getMessageId());
            }
        } catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            e.printStackTrace();
        }
        //在应用退出前，销毁Producer对象
        producer.shutdown();
    }
}