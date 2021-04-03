package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-17 22:48
 */
@SpringBootApplication
@EnableEurekaClient//开启eureka client的注解
public class PaymentMain8001 {

    public static void main(String [] args){
        SpringApplication.run(PaymentMain8001.class,args);
    }

}
