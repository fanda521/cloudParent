package com.wang.springboot.controller;

import com.wang.springboot.entities.CommonResult;
import com.wang.springboot.entities.Payment;
import com.wang.springboot.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private PaymentService paymentService;

    @PostMapping("payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int i = paymentService.create(payment);
        log.info("*******插入的结果8003："+i);
        if (i>0){
            return new CommonResult(200,"插入成功8003",i);

        }
        else {
            return new CommonResult(444,"插入失败8003",null);
        }
    }

    @GetMapping("payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment i = paymentService.getPaymentById(id);
        log.info("*******插入的结果8003："+i);
        if (i != null){
            return new CommonResult(200,"查询成功8003",i);

        }
        else {
            return new CommonResult(444,"查询失败8003",null);
        }
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

}
