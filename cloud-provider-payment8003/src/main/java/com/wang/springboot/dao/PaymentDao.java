package com.wang.springboot.dao;

import com.wang.springboot.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-17 23:07
 */
@Mapper
public interface PaymentDao {
    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
