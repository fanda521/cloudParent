package com.wang.springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-31 14:28
 */
@RestController
@RefreshScope //支持nacos的动态刷新功能
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @Value("${config.service-provider.version}")
    private String serviceProviderVersion;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/config/info")
    public String getConfigInfo(){
        return configInfo;
    }


    @GetMapping("/config/service-provider/version")
    public String getServiceProviderVersion(){
        return "port:" + serverPort + "  version:" + serviceProviderVersion;
    }

}
