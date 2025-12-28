package com.wang.springboot.controller;

import com.wang.springboot.openfeign_client.ProviderPaymentOpenFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
public class OrderNacosConsumerController {

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @Resource
    private RestTemplate restTemplate;

    @Value("${config.service-provider.version}")
    private String serviceProviderVersion;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private ProviderPaymentOpenFeignClient providerPaymentOpenFeignClient;


    @GetMapping("/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Integer id){

        return restTemplate.getForObject(serverURL+"/payment/nacos/"+id,String.class);
    }

    @GetMapping("/consumer/payment/openfeign/gray")
    public String paymentInfoOpenFeign(){
        return providerPaymentOpenFeignClient.getPaymentGray();
    }

    @GetMapping("/config/service-provider/version")
    public String getServiceProviderVersion(){
        return "port:" + serverPort + "  version:" + serviceProviderVersion;
    }


}
