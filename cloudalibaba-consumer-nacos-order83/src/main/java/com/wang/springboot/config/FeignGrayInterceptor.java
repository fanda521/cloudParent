package com.wang.springboot.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign拦截器：将Consumer收到的灰度标签，透传给下游Provider
 * ✅ 实现全链路灰度
 */
@Component
public class FeignGrayInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 获取当前请求上下文
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        // ✅ 核心：透传灰度标签请求头
        String grayVersion = request.getHeader("Gray-Version");
        if (grayVersion != null) {
            requestTemplate.header("Gray-Version", grayVersion);
        }
    }
}