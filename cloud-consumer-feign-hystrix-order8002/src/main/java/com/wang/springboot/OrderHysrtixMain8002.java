package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-09-05 16:33
 */
@SpringBootApplication
@EnableFeignClients
@EnableHystrix//使用hystrix fallback
public class OrderHysrtixMain8002 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHysrtixMain8002.class,args);
    }


}
