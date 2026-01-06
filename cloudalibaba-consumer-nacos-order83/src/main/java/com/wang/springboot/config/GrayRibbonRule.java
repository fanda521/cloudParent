package com.wang.springboot.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * ✅ Spring Cloud Alibaba 2.1.0.RELEASE 专属灰度负载均衡规则
 * 实现 Ribbon 原生 AbstractLoadBalancerRule（IRule）接口，适配低版本核心API
 * 核心能力：按Nacos元数据version筛选实例、支持prod/gray/all三种灰度策略
 */
@Slf4j
@Component
public class GrayRibbonRule extends AbstractLoadBalancerRule {

    // 注入Nacos注册发现客户端（2.1.0.RELEASE 原生支持）
    @Autowired
    private DiscoveryClient discoveryClient;

    // 注入灰度规则配置类（复用原有，已开启动态刷新@RefreshScope）
    @Autowired
    private GrayRuleConfig grayRuleConfig;

    // 目标服务名（与提供者一致：service-provider）
    private static final String TARGET_SERVICE_ID = "nacos-payment-provider";

    // 轮询索引（保证负载均衡均匀性，线程安全）
    private int currentIndex = 0;

    // 计数器：记录当前调用次数
    private int callCount = 0;

    // 调用周期（默认10次）
    private int periodCount = 10;

    // 计数器：记录目标版本已调用次数
    private int targetVersionCount = 0;

    /**
     * ✅ Ribbon核心方法：重写负载均衡规则，返回选中的服务实例
     */
    public Server choose(Object key) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        if (loadBalancer == null) {
            return null;
        }
        String targetServiceName = null;
        if (loadBalancer instanceof BaseLoadBalancer) {
            BaseLoadBalancer baseLB = (BaseLoadBalancer) loadBalancer;
            targetServiceName =  baseLB.getName(); // ✅ 直接返回服务名，与@FeignClient("xxx")完全一致
        }
        System.out.println("✅ Ribbon负载均衡-目标服务名 = " + targetServiceName);
        // 1. 获取目标服务下的所有可用实例（Nacos注册中心）
        List<ServiceInstance> allInstances = discoveryClient.getInstances(targetServiceName);
        if (allInstances == null || allInstances.isEmpty()) {
            log.error("【灰度规则】服务{}无可用实例！", targetServiceName);
            return null;
        }

        // 2. 核心：根据Nacos动态灰度规则，筛选符合版本的实例
        List<ServiceInstance> targetInstances = filterServiceInstance(allInstances);
        if (targetInstances.isEmpty()) {
            log.error("【灰度规则】服务{}无符合{}版本的实例！", TARGET_SERVICE_ID, grayRuleConfig.getVersion());
            return null;
        }

        // 3. 对筛选后的实例执行轮询负载均衡，返回最终选中实例
        ServiceInstance selectedInstance = roundRobin(targetInstances);
        
        // 4. 转换为Ribbon的Server对象返回（2.1.0.RELEASE 必须转换）
        return new Server(selectedInstance.getHost(), selectedInstance.getPort());
    }

//    /**
//     * ✅ 核心逻辑：按灰度版本筛选实例（prod/gray/all）
//     */
//    private List<ServiceInstance> filterServiceInstance(List<ServiceInstance> allInstances) {
//        String targetVersion = grayRuleConfig.getVersion();
//        // 策略1：all → 返回全部实例，走全量负载均衡
//        if ("all".equalsIgnoreCase(targetVersion)) {
//            return allInstances;
//        }
//        // 策略2：prod/gray → 精准匹配Nacos实例元数据的version字段
//        return allInstances.stream()
//                .filter(instance -> targetVersion.equals(instance.getMetadata().get("version")))
//                .collect(Collectors.toList());
//    }


    private List<ServiceInstance> filterServiceInstance(List<ServiceInstance> allInstances) {
        List<ServiceInstance> resultInstances = allInstances;
        String targetVersion = grayRuleConfig.getVersion();
        BigDecimal rate = grayRuleConfig.getRate();
        int grayCount = rate.multiply(BigDecimal.valueOf(10), MathContext.UNLIMITED).intValue();

        // 如果没有设置比率或者为null，使用原有的逻辑
        if (rate == null || "all".equalsIgnoreCase(targetVersion)) {
            return resultInstances;
        }

        // 分离目标版本和其他版本的实例
        List<ServiceInstance> targetInstances = allInstances.stream()
                .filter(instance -> targetVersion.equals(instance.getMetadata().get("version")))
                .collect(Collectors.toList());

        List<ServiceInstance> otherInstances = allInstances.stream()
                .filter(instance -> !targetVersion.equals(instance.getMetadata().get("version")))
                .collect(Collectors.toList());

        if (targetInstances.isEmpty() && otherInstances.isEmpty()) {
            return resultInstances;
        }

        if (targetInstances.isEmpty()) {
            return otherInstances;
        }

        if (otherInstances.isEmpty()) {
            return targetInstances;
        }

        // 同步块确保线程安全
        synchronized (this) {
            if (targetVersionCount < grayCount) {
                // 计算每轮的总调用次数和目标调用次数
                double random = Math.random();
                if (random <= rate.doubleValue()) {
                    targetVersionCount++;
                    resultInstances = targetInstances;
                } else {
                    if (callCount - targetVersionCount < periodCount - grayCount){
                        resultInstances = otherInstances;
                    } else {
                        targetVersionCount++;
                        resultInstances = targetInstances;
                    }
                }
            } else {
                resultInstances = otherInstances;
            }
            callCount++;
            if (callCount >= periodCount) {
                callCount = 0;
                targetVersionCount = 0;
            }
            return resultInstances;
        }
    }




    /**
     * ✅ 轮询负载均衡算法（线程安全，适配2.1.0同步调用）
     */
    private ServiceInstance roundRobin(List<ServiceInstance> instances) {
        if (instances.size() == 1) {
            return instances.get(0);
        }
        // 加锁保证多线程下索引不混乱，生产级安全处理
        synchronized (this) {
            if (currentIndex >= instances.size()) {
                currentIndex = 0;
            }
            return instances.get(currentIndex++);
        }
    }

    // ========== 以下为Ribbon接口必须实现的方法，2.1.0.RELEASE 固定写法 ==========
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // 初始化配置，2.1.0.RELEASE 无需额外处理，空实现即可
    }
}