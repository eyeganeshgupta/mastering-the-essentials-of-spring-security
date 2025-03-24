package io.spring.security.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.spring.entity.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // @Value("${jwt.secret-key}")
    private final String secretKey;

    public JwtUtil(Dotenv dotenv) {
        this.secretKey = dotenv.get("JWT_SECRET_KEY");
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        long fourteenDaysInMillis = 14L * 24 * 60 * 60 * 1000;

        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuer("universe")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + fourteenDaysInMillis))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        // openssl rand -base64 32
        byte[] decode = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }
}
