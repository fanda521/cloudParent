package com.wang.springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-25 20:35
 */
@RestController
public class OrderNacosConsumerController {

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Integer id){

        return restTemplate.getForObject(serverURL+"/payment/nacos/"+id,String.class);
    }


}
