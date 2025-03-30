package io.spring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {

    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> tenantContext = new ThreadLocal<>();

    public static void setTenant(String tenantId) {
        logger.info("Setting tenant in TenantContext: {}", tenantId);
        tenantContext.set(tenantId);
    }

    public static String getTenant() {
        String tenant = tenantContext.get();
        if (tenant != null) {
            logger.info("Retrieved tenant from TenantContext: {}", tenant);
        } else {
            logger.warn("No tenant set in TenantContext");
        }
        return tenant;
    }

    public static void clear() {
        logger.info("Clearing tenant context");
        tenantContext.remove();
    }
}
