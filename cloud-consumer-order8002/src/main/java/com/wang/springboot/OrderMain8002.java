package com.wang.springboot;

import com.wang.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-18 22:03
 */
@SpringBootApplication
@EnableEurekaClient//开启eureka client的注解
//注意name的值是注册中心服务的名字，如果是大写，这里也要是大写，大小写敏感
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = MySelfRule.class)//替换负载均衡规则的注解
public class OrderMain8002 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain8002.class,args);
    }
}
