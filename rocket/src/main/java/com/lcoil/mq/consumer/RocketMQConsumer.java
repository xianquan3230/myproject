package com.lcoil.mq.consumer;
import com.aliyun.openservices.ons.api.*;
import com.lcoil.mq.contant.MqTag;
import com.lcoil.mq.config.RocketMQConfig;
import com.lcoil.mq.contant.MqTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Properties;

/**
 * @Classname MqApplication
 * @Description RocketMQ消费者
 * @Date 2022/5/15 7:47 PM
 * @Created by l-coil
 */
@Component
@Slf4j
public class RocketMQConsumer {

    @Autowired
    private RocketMQConfig rocketMQConfig;

    /**
     * 订阅消息，处理业务
     */
    public void normalSubscribe() {
        Properties properties = rocketMQConfig.getProperties();
        properties.put(PropertyKeyConst.GROUP_ID, RocketMQConsumer.class.getSimpleName());
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(MqTopic.TEST_TOPIC, "", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                try {
                    //接收到的消息内容
                    String msg = new String(message.getBody(), "UTF-8");
                    String tag = message.getTag();
                    switch (tag) {
                        case MqTag.ROCKETMQ_TEST1:
                            log.info("收到消息messageID：" + message.getMsgID() + " msg:" + msg);
                            //TODO do something
                            break;
                        case  MqTag.ROCKETMQ_TEST2:
                            log.info("收到消息messageID：" + message.getMsgID() + " msg:" + msg);
                            //TODO do something
                            break;
                    }
                    return Action.CommitMessage;
                } catch (Exception e) {
                    log.info("消费失败：messageID：" + message.getMsgID());
                    e.printStackTrace();
                    return Action.ReconsumeLater;
                }
            }
        });
        consumer.start();
    }
}