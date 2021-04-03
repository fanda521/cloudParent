package com.wang.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-19 11:18
 */
@SpringBootApplication
@EnableEurekaServer//开启服务注册中心的注解
public class EurekaServerMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerMain7001.class,args);
    }
}
