package io.spring.config;

import io.spring.interceptor.TenantInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    private final TenantInterceptor tenantInterceptor;

    public WebConfig(TenantInterceptor tenantInterceptor) {
        logger.info("WebConfig Constructor");
        this.tenantInterceptor = tenantInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("Registering TenantInterceptor to intercept all paths (/**)");

        registry.addInterceptor(tenantInterceptor).addPathPatterns("/**");

        logger.info("TenantInterceptor has been successfully registered.");
    }
}
