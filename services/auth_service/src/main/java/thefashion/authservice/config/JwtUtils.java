package thefashion.authservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import thefashion.authservice.domain.dto.authentication.CustomUserDetails;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtils {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder refreshJwtEncoder;
    private final JwtDecoder refreshJwtDecoder;
    private final JwtConfig jwtConfig;

    public JwtUtils(
            JwtEncoder jwtEncoder,
            JwtDecoder jwtDecoder,
            @Qualifier("refreshJwtEncoder") JwtEncoder refreshJwtEncoder,
            @Qualifier("refreshJwtDecoder") JwtDecoder refreshJwtDecoder,
            JwtConfig jwtConfig
    ) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.refreshJwtEncoder = refreshJwtEncoder;
        this.refreshJwtDecoder = refreshJwtDecoder;
        this.jwtConfig = jwtConfig;
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();
        long expirationSeconds = jwtConfig.getAccessTokenExpiration() / 1000;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .issuedAt(now)
                .expiresAt(now.plus(expirationSeconds, ChronoUnit.SECONDS))
                .subject(userDetails.getEmail())
                .claim("userId", userDetails.getUserId().toString())
                .claim("role", userDetails.getRole())
                .claim("firstName", userDetails.getFirstName())
                .claim("lastName", userDetails.getLastName())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Instant now = Instant.now();
        long expirationSeconds = jwtConfig.getRefreshTokenExpiration() / 1000;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .issuedAt(now)
                .expiresAt(now.plus(expirationSeconds, ChronoUnit.SECONDS))
                .subject(userDetails.getEmail())
                .claim("userId", userDetails.getUserId().toString())
                .claim("type", "refresh")
                .build();

        return refreshJwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    public String extractUserId(String token) {
        return jwtDecoder.decode(token).getClaim("userId");
    }

    public String extractRole(String token) {
        return jwtDecoder.decode(token).getClaim("role");
    }

    public Instant extractExpiration(String token) {
        return jwtDecoder.decode(token).getExpiresAt();
    }

    public boolean isTokenExpired(String token) {
        try {
            Instant expiration = extractExpiration(token);
            return expiration != null && expiration.isBefore(Instant.now());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            refreshJwtDecoder.decode(token);
            Jwt jwt = refreshJwtDecoder.decode(token);
            return jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUserIdFromRefreshToken(String token) {
        return refreshJwtDecoder.decode(token).getClaim("userId");
    }

    public String extractUsernameFromRefreshToken(String token) {
        return refreshJwtDecoder.decode(token).getSubject();
    }
}
