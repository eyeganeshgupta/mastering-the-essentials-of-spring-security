package io.spring.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AzpUriMatchingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AzpUriMatchingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("Received request URI: {}", httpRequest.getRequestURI());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication: {}", authentication);

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String azp = (String) jwtAuthenticationToken.getToken().getClaims().get("azp");
            String uri = httpRequest.getRequestURI();
            String[] pathSegments = uri.split("/");
            String tenantIdFromUri = pathSegments[3];

            logger.info("Extracted azp from JWT: {}", azp);
            logger.info("Extracted tenantId from URI: {}", tenantIdFromUri);

            if (azp != null && !azp.equalsIgnoreCase(tenantIdFromUri)) {
                logger.warn("AZP-Tenant mismatch: {} vs {}", azp, tenantIdFromUri);
                /*
                 * throw new
                 * TenantMismatchException("The tenant in the request URI does not match the tenant ID in the JWT authorization."
                 * );
                 */

                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("""
                        {
                            "error": "tenant_mismatch",
                            "message": "Request tenant does not match JWT authorization"
                        }
                        """);
                return;

            }
        }

        chain.doFilter(request, response);
    }
}
