package com.wang.springboot.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-30 15:27
 */
@Configuration
public class LogConfig {
    @Bean
    public Logger.Level feiginLoggerLevel(){
        return Logger.Level.FULL;
    }
}
