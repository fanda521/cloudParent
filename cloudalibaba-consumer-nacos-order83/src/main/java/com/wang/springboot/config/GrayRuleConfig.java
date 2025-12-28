package com.wang.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RefreshScope // ✅ 开启配置动态刷新（核心注解）
public class GrayRuleConfig {
    // 对应Nacos配置中的 service-provider.version
    @Value("${config.service-provider.version}")
    private String version;


    // getter/setter 必须
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}