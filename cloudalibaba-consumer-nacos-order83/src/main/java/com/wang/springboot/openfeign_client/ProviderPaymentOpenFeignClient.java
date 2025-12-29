package com.wang.springboot.openfeign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lucksoul
 * @version 1.0
 * @date 2025/12/28 17:12
 */
@Component
@FeignClient(value = "nacos-payment-provider")
public interface ProviderPaymentOpenFeignClient {

    @GetMapping("/payment/nacos/gray")
    String getPaymentGray();
}
