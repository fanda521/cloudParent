package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-25 20:33
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderConsumerMain83 {
    public static void main(String[] args) {
        SpringApplication.run(OrderConsumerMain83.class,args);
    }
}
