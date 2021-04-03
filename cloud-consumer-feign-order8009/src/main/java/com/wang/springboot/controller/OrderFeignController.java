package com.wang.springboot.controller;

import com.wang.springboot.entities.CommonResult;
import com.wang.springboot.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-29 19:12
 */
@RestController
@Slf4j
public class OrderFeignController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        return paymentService.getPaymentById(id);
    }

    @GetMapping("consumer/payment/feign/timeout")
    public String paymentFeignTimeOut(){
        return paymentService.paymentFeignTimeOut();
    }
}
