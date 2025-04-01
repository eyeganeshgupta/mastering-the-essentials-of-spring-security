package io.spring.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.spring.utils.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TenantInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("Entering TenantInterceptor - preHandle method");

        String tenant = extractTenantFromPath(request.getRequestURI());
        if (tenant != null) {
            TenantContext.setTenant(tenant);
            logger.info("Tenant extracted from URI: {} and set in TenantContext", tenant);
        } else {
            logger.info("No tenant extracted from URI: {}", request.getRequestURI());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView)
            throws Exception {
        logger.info("Entering TenantInterceptor - postHandle method");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("Entering TenantInterceptor - afterCompletion method");
    }

    private String extractTenantFromPath(String uri) {
        logger.info("Extracting tenant from URI: {}", uri);

        String[] uriParts = uri.split("/");
        if (uriParts.length > 1) {
            String tenant = uriParts[3];
            logger.info("Extracted tenant: {}", tenant);
            return tenant;
        }
        return null;
    }
}
