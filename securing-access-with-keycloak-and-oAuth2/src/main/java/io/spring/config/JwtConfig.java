package io.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    @Value("${security.jwt.key-set-uri}")
    private String jwtKeySetUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        logger.info("Initializing JwtDecoder with key-set URI: {}", jwtKeySetUri);
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwtKeySetUri).build();
        logger.info("JwtDecoder successfully created.");
        return jwtDecoder;
    }
}
