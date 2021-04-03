package com.wang.springboot.lib;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-29 14:17
 */
public interface LoadBalancer {
    ServiceInstance instance(List<ServiceInstance> serviceInstances);
}
