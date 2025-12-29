package com.wang.springboot.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ✅ Spring Cloud Alibaba 2.1.0.RELEASE Ribbon灰度配置类
 * 作用：将自定义灰度规则绑定到指定服务，实现精准灰度路由
 */
// 方式1：单服务灰度（推荐）- 仅对指定服务生效，填写你的服务提供者名称（如gray-provider）
@RibbonClient(
        name = "nacos-payment-provider",
        configuration = RibbonGrayConfig.GrayRuleInnerConfig.class
)

// 方式2：全局灰度（所有服务生效）- 注释上方，开启下方即可
// @RibbonClients(defaultConfiguration = RibbonGrayConfig.GrayRuleInnerConfig.class)
@Configuration
public class RibbonGrayConfig {

    /**
     * 内部配置类：注册灰度规则Bean（2.1.0版本强制写法，保证规则生效）
     */
    @Configuration
    public static class GrayRuleInnerConfig {
        /**
         * 注册自定义灰度规则，替换Ribbon默认的轮询规则
         */
        @Bean
        public IRule grayRule() {
            return new GrayRibbonRule();
        }
    }
}