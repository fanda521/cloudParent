package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-16 19:20
 */
@SpringBootApplication
@EnableEurekaClient
public class MainConfigClient3355 {
    public static void main(String[] args) {
        SpringApplication.run(MainConfigClient3355.class,args);
    }
}
