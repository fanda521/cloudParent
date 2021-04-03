package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-29 9:41
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8008 {

    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8008.class,args);
    }

}
