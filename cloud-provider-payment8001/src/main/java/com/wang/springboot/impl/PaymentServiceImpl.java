package com.wang.springboot.impl;

import com.wang.springboot.dao.PaymentDao;
import com.wang.springboot.entities.Payment;
import com.wang.springboot.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-17 23:24
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
