package com.wang.springboot.controller;


import com.wang.springboot.entities.CommonResult;
import com.wang.springboot.entities.Payment;
import com.wang.springboot.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-17 23:26
 */
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;
    @Resource
    private DiscoveryClient discoveryClient;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int i = paymentService.create(payment);
        log.info("*******插入的结果："+i);
        if (i>0){
            return new CommonResult(200,"插入成功",i);

        }
        else {
            return new CommonResult(444,"插入失败",null);
        }
    }

    @GetMapping("payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment i = paymentService.getPaymentById(id);
        log.info("*******插入的结果："+i);
        if (i != null){
            return new CommonResult(200,"查询成功",i);

        }
        else {
            return new CommonResult(444,"查询失败",null);
        }
    }

    @GetMapping(value = "/payment/discovery/")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (int i = 0; i < services.size(); i++) {
            String s = services.get(i);
            System.out.println(s);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (int i = 0; i < instances.size(); i++) {
            ServiceInstance serviceInstance = instances.get(i);
            System.out.println("host:"+serviceInstance.getHost());
            System.out.println("instanceId:"+serviceInstance.getInstanceId());
            System.out.println("metadata:"+serviceInstance.getMetadata());
            System.out.println("port:"+serviceInstance.getPort());
            System.out.println("scheme:"+serviceInstance.getScheme());
            System.out.println("serviceId:"+serviceInstance.getServiceId());
            System.out.println("uri:"+serviceInstance.getUri());
        }
        return instances;
    }


    @GetMapping("/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeOut(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    @GetMapping("/payment/zipkin")
    public String paymentZipkin(){
        return "hi, iam paymentzipkin server fall back,,welcomen to lucksoul";
    }


}
