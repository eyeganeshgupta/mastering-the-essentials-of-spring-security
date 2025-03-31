package io.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakRoleConverter.class);

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_KEY = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        logger.info("Converting JWT to GrantedAuthorities");

        Map<String, Object> realmAccess = getRealmAccessClaim(jwt);

        if (realmAccess == null) {
            logger.info("No 'realm_access' claim found in JWT");
            return List.of();
        }

        if (!realmAccess.containsKey(ROLES_KEY)) {
            logger.info("No roles found in 'realm_access' claim");
            return List.of();
        }

        List<String> roles = (List<String>) realmAccess.get(ROLES_KEY);
        logger.info("Found roles in 'realm_access' claim: {}", roles);

        return roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getRealmAccessClaim(Jwt jwt) {
        return (Map<String, Object>) jwt.getClaims().get(REALM_ACCESS_CLAIM);
    }
}
