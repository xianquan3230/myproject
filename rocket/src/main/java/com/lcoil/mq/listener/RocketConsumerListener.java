package com.lcoil.mq.listener;

import com.lcoil.mq.consumer.RocketMQConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Classname RocketConsumerListener
 * @Description RocketMQ启动监听
 * @Date 2022/5/15 7:47 PM
 * @Created by l-coil
 */
@Component
public class RocketConsumerListener implements CommandLineRunner {

    @Autowired
    private RocketMQConsumer rocketMQConsumer;

    @Override
    public void run(String... args) {
        System.out.println("========rocketMQ消费者启动==========");
        rocketMQConsumer.normalSubscribe();
    }
}