package com.wang.sprinbboot;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-09-05 11:59
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker//fallbackd 时使用加的
public class HystrixMain8101 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixMain8101.class,args);
    }

    /**
     * 此配置时为了服务监控配置，与服务熔断无关，springboot升级后的坑
     * servlerregisterationBean因为springboot的默认路径不是“、hystrix.stream”
     * 只要在自己的项目里面配置上，下面的servlet就可以了
     */

    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
