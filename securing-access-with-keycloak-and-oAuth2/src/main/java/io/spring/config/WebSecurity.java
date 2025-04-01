package io.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

        private static final Logger logger = LoggerFactory.getLogger(WebSecurity.class);

        @Value("${security.roles.admin}")
        private String adminRole;

        @Value("${security.roles.sales}")
        private String salesRole;

        @Value("${security.roles.product_manager}")
        private String productManagerRole;

        @Value("${security.roles.test_engineer}")
        private String testEngineerRole;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

                logger.info("Configuring security settings with JWT authentication for roles: {} | {} | {} | {}",
                                adminRole, salesRole, productManagerRole, testEngineerRole);

                return httpSecurity
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/{tenantId}/smartphones")
                                                .hasAnyRole(adminRole, salesRole, productManagerRole)
                                                .requestMatchers(HttpMethod.GET, "/api/v1/{tenantId}/smartphones/{id}")
                                                .hasAnyRole(adminRole, salesRole, productManagerRole, testEngineerRole)
                                                .requestMatchers(HttpMethod.POST, "/api/v1/{tenantId}/smartphones")
                                                .hasAnyRole(adminRole, productManagerRole)
                                                .requestMatchers(HttpMethod.PUT, "/api/v1/{tenantId}/smartphones/**")
                                                .hasRole(adminRole)
                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/{tenantId}/smartphones/**")
                                                .hasRole(adminRole)
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .oauth2ResourceServer(resourceServer -> resourceServer
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                                .addFilterAfter(
                                                new AzpUriMatchingFilter(),
                                                BearerTokenAuthenticationFilter.class)
                                .build();
        }
}
