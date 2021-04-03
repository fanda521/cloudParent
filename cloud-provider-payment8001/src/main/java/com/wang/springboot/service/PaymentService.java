package com.wang.springboot.service;

import com.wang.springboot.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-17 23:22
 */
public interface PaymentService {

    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
