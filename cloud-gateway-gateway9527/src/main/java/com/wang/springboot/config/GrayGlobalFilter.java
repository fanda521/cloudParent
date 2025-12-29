package com.wang.springboot.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Random;

/**
 * ✅ 2.1.0版本网关全局灰度过滤器
 * 核心：解析Nacos灰度规则、透传Gray-Version标签，所有流量统一入口处理
 * 适配Gateway 2.1.x API，无版本冲突
 */
@Component
public class GrayGlobalFilter implements GlobalFilter, Ordered {
    // 灰度规则（可绑定Nacos配置，2.1.0版本@RefreshScope正常生效）
    private final String DEFAULT_VERSION = "v1.0";
    private final String GRAY_VERSION = "v2.0";
    private final Integer GRAY_WEIGHT = 10; // 10%流量灰度
    private final Random random = new Random();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange newExchange = exchange;
        String targetVersion = DEFAULT_VERSION;

        // 策略1：请求头灰度（优先级最高）
        String headerGray = exchange.getRequest().getHeaders().getFirst("Gray-Version");
        if (headerGray != null && !headerGray.isEmpty()) {
            targetVersion = headerGray;
        }
        // 策略2：用户ID区间灰度（User-Id:100-200走灰度）
        else {
            String userId = exchange.getRequest().getHeaders().getFirst("User-Id");
            if (userId != null) {
                try {
                    int uid = Integer.parseInt(userId);
                    if (uid >= 100 && uid <= 200) {
                        targetVersion = GRAY_VERSION;
                    }
                } catch (Exception e) {
                    targetVersion = DEFAULT_VERSION;
                }
            }
            // 策略3：权重灰度（兜底策略）
            else {
                int randomNum = random.nextInt(100);
                if (randomNum < GRAY_WEIGHT) {
                    targetVersion = GRAY_VERSION;
                }
            }
        }


        // ✅ 核心：向请求上下文透传灰度标签（Ribbon规则可直接读取）
        newExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("Gray-Version", targetVersion)
                        .build())
                .build();

        return chain.filter(newExchange);
    }

    // 过滤器优先级：最高（保证灰度标签最先透传）
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}