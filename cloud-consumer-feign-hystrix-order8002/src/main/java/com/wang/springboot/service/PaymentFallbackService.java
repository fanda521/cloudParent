package com.wang.springboot.service;

import org.springframework.stereotype.Component;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-09-05 17:41
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    public String paymentInfo_OK(Integer id) {
        return "-----PaymentFallbackService  fall back paymentInfo_OK ,~~~~(>_<)~~~~ ";
    }

    public String paymentInfo_TimeOut(Integer id) {
        return "-----PaymentFallbackService  fall back paymentInfo_TimeOut ,~~~~(>_<)~~~~ ";
    }
}
