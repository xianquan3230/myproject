package com.lcoil.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Classname EsApplication
 * @Description TODO
 * @Date 2022/5/15 12:46 PM
 * @Created by l-coil
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class,args);
    }
}
