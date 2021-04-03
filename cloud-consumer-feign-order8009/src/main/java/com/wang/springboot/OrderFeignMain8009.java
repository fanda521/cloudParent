package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-29 19:03
 */
@SpringBootApplication
@EnableFeignClients
public class OrderFeignMain8009 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain8009.class,args);
    }
}
