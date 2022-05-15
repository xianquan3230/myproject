package com.lcoil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Classname LoginApplication
 * @Description TODO
 * @Date 2022/5/13 2:41 PM
 * @Created by l-coil
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class,args);
    }
}
