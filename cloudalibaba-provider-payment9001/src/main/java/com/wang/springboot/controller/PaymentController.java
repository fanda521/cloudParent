package com.wang.springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-25 20:35
 */
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.cloud.nacos.discovery.metadata.version}")
    private String grayVersion;

    @Value("${spring.cloud.nacos.discovery.metadata.env}")
    private String grayEnv;

    @Value("${spring.cloud.nacos.discovery.metadata.gray-weight}")
    private String grayWeight;

    @GetMapping("/payment/nacos/{id}")
    public String getPayment(@PathVariable("id") Integer id){

        return "nacos registry,serverPosrt:"+serverPort+"\t id:"+id;
    }

    @GetMapping("/payment/nacos/gray")
    public String getPaymentGray(){

        return "nacos registry,serverPosrt:"+serverPort +",grayVersion:" + grayVersion
                + ",grayEnv:" + grayEnv + ",grayWeight:" + grayWeight;
    }


}
