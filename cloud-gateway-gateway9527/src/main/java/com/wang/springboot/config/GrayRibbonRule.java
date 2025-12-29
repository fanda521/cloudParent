package com.wang.springboot.config;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * ✅ Spring Cloud Alibaba 2.1.0.RELEASE 灰度负载均衡规则（Ribbon版）
 * 核心能力：根据Gateway透传的Gray-Version请求头 ↔ Nacos实例metadata.version 精准匹配
 * 底层依赖：Netflix Ribbon，完全适配2.1.0版本技术栈
 */
@Component
public class GrayRibbonRule extends AbstractLoadBalancerRule {
    // 随机数工具（负载均衡选实例）
    private final Random random = new Random();
    // 默认正式版本（兜底）
    private static final String DEFAULT_VERSION = "v1.0";
    // 灰度标签请求头（与Gateway过滤器一致）
    private static final String GRAY_HEADER_KEY = "Gray-Version";

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // Ribbon规则初始化方法，空实现即可（2.1.0版本无需额外配置）
    }

    @Override
    public Server choose(Object key) {
        // 1. 获取Ribbon负载均衡器，校验非空
        ILoadBalancer lb = getLoadBalancer();
        if (lb == null) {
            return null;
        }
        
        // 2. 核心：从请求上下文中解析灰度标签（2.1.0版本通用方式）
        String targetGrayVersion = getGrayVersionFromContext(key);
        
        // 3. 获取服务实例列表，过滤健康可用实例
        List<Server> allServers = lb.getAllServers();
        List<Server> upServers = lb.getReachableServers(); // 仅筛选健康实例（生产必备）
        if (upServers.isEmpty()) {
            return null;
        }

        // 4. 灰度实例过滤：匹配 Nacos元数据version = 灰度标签
        List<Server> grayMatchedServers = upServers.stream()
                .filter(server -> {
                    // 强转为Nacos注册的实例，读取元数据（2.1.0版本固定写法）
                    if (server instanceof NacosServer) {
                        NacosServer nacosServer = (NacosServer) server;
                        String instanceVersion = nacosServer.getMetadata().get("version");
                        return targetGrayVersion.equals(instanceVersion);
                    }
                    return false;
                })
                .collect(Collectors.toList());

        // 5. 双层兜底策略（生产级健壮性）
        List<Server> targetServers;
        if (!grayMatchedServers.isEmpty()) {
            targetServers = grayMatchedServers; // 有灰度实例 → 走灰度
        } else {
            // 无灰度实例 → 兜底走正式版v1
            targetServers = upServers.stream()
                    .filter(server -> {
                        if (server instanceof NacosServer) {
                            String instanceVersion = ((NacosServer) server).getMetadata().get("version");
                            return DEFAULT_VERSION.equals(instanceVersion);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            // 最终兜底：连正式版都无 → 返回空
            if (targetServers.isEmpty()) {
                return null;
            }
        }

        // 6. 负载均衡选实例（随机策略，2.1.0版本最稳定，可替换为轮询）
        return chooseRandomServer(targetServers);
    }

    /**
     * 2.1.0版本核心：从请求上下文中解析灰度标签Gray-Version
     */
    private String getGrayVersionFromContext(Object key) {
        try {
            // 解析Gateway透传的请求上下文，获取灰度标签
            if (key instanceof ServerWebExchange) {
                ServerWebExchange exchange = (ServerWebExchange) key;
                ServerHttpRequest request = exchange.getRequest();
                String grayVersion = request.getHeaders().getFirst(GRAY_HEADER_KEY);
                return grayVersion == null ? DEFAULT_VERSION : grayVersion;
            }
        } catch (Exception e) {
            // 异常兜底：任何解析失败，均走正式版，保证服务可用
            return DEFAULT_VERSION;
        }
        return DEFAULT_VERSION;
    }

    /**
     * 随机选择实例（Ribbon原生策略，2.1.0版本无兼容问题）
     */
    private Server chooseRandomServer(List<Server> servers) {
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}