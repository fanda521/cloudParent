package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-16 23:22
 */
@SpringBootApplication
@EnableEurekaClient
public class MainConfigClient3366 {
    public static void main(String[] args) {
        SpringApplication.run(MainConfigClient3366.class,args);
    }
}
