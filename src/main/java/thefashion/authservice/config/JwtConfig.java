package thefashion.authservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thefashion.authservice.domain.dto.authentication.CustomUserDetails;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKeyString;

    @Value("${jwt.expiration}")
    private Long accessTokenExpiration; // 24 hours in milliseconds

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshTokenExpiration; // 7 days in milliseconds

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .claim("userId", userDetails.getUserId().toString())
                .claim("role", userDetails.getRole())
                .claim("firstName", userDetails.getFirstName())
                .claim("lastName", userDetails.getLastName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessTokenExpiration)))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .claim("userId", userDetails.getUserId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(refreshTokenExpiration)))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}