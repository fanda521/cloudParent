package com.wang.springboot.controller;

import com.wang.springboot.entities.CommonResult;
import com.wang.springboot.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-18 22:05
 */
@RestController
@Slf4j
public class OrderController {

    @Resource
    private RestTemplate restTemplate;

    //单机版
    //public static  final String PAYMENT_URL = "http://localhost:8001";
    //集群版
    public static  final String PAYMENT_URL = "http://cloud-provider-consul-payment";

    @GetMapping(value = "/consul/consumer")
    public String paymentInfo(){
        return restTemplate.getForObject(PAYMENT_URL+"/payment/consul",String.class);
    }

}
