package com.lcoil;

import com.apple.eawt.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Classname WebHookApplication
 * @Description TODO
 * @Date 2022/5/16 5:08 PM
 * @Created by l-coil
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WebHookApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebHookApplication.class,args);
        System.out.println("webhook-server start success！");
    }
}
