package com.wang.springboot.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ✅ Spring Cloud Alibaba 2.1.0.RELEASE Ribbon配置类
 * 注入自定义灰度负载均衡规则，替换默认的轮询/随机规则
 */
@Configuration
public class RibbonGrayConfig {

    /**
     * 将自定义灰度规则注入容器，Ribbon自动识别并替换默认规则
     */
    //@Bean
    public IRule grayRule() {
        return new GrayRibbonRule();
    }
}