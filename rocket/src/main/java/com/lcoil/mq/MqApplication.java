package com.lcoil.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Classname MqApplication
 * @Description TODO
 * @Date 2022/5/15 7:47 PM
 * @Created by l-coil
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MqApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqApplication.class,args);
        System.out.println("rocketmq-server start success！");
    }
}
