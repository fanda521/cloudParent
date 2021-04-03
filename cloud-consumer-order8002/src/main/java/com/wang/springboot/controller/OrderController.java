package com.wang.springboot.controller;

import com.wang.springboot.entities.CommonResult;
import com.wang.springboot.entities.Payment;
import com.wang.springboot.lib.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-18 22:05
 */
@RestController
@Slf4j
public class OrderController {

    @Resource
    private LoadBalancer loadBalancer;
    //自定义的负载方法

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;

    //单机版
    //public static  final String PAYMENT_URL = "http://localhost:8001";
    //集群版
    public static  final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    @GetMapping("consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }

    @GetMapping("consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
    }

    /**
     * 测试getForEntity
     * @param id
     * @return
     */
    @GetMapping("consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        if (forEntity.getStatusCode().is2xxSuccessful()){
            return forEntity.getBody();
        }
        else {
            return new CommonResult<Payment>(444,"fail");
        }
    }

    /**
     * 自定义负载方法
     * @return
     */
    @GetMapping("/consumer/payment/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(instances==null || instances.size()<=0){
            return null;
        }
        ServiceInstance instance = loadBalancer.instance(instances);
        URI uri = instance.getUri();
        return restTemplate.getForObject(uri+"/payment/lb",String.class);

    }

    // zipkin+sleuth
    @GetMapping("/consumer/payment/zipkin")
    public String paymentZin(){
        String result =restTemplate.getForObject("http://localhost:8001"+"payment/zipkin/",String.class);
        return result;
    }

}
