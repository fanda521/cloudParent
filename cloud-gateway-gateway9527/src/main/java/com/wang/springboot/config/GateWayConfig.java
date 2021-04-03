package com.wang.springboot.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-16 14:39
 */
@Configuration
public class GateWayConfig {

    /**
     * 当访问地址http://localhost:9527/guonei 自动转发到 http://news.baidu.com/guonei
     *
     * @param routeLocatorBuilder
     * @return
     */

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){

        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_myRoute1",r -> r.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        return routes.build();
    }

}
