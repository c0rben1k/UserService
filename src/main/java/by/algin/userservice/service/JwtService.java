package by.algin.userservice.service;

import by.algin.userservice.config.AppProperties;
import by.algin.userservice.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final AppProperties appProperties;

    public String generateAccessToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User userPrincipal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return generateToken(userPrincipal, appProperties.getSecurity().getJwt().getAccessTokenExpiration());
    }

    public String generateRefreshToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User userPrincipal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return generateToken(userPrincipal, appProperties.getSecurity().getJwt().getRefreshTokenExpiration());
    }

    public String generateAccessToken(User user) {
        return generateToken(createClaims(user), user.getUsername(), appProperties.getSecurity().getJwt().getAccessTokenExpiration());
    }

    private String generateToken(org.springframework.security.core.userdetails.User userPrincipal, long expiration) {
        Map<String, Object> claims = new HashMap<>();

        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claims.put("authorities", authorities);

        return generateToken(claims, userPrincipal.getUsername(), expiration);
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        String authorities = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(","));

        claims.put("authorities", authorities);
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        return claims;
    }

    private String generateToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getSecurity().getJwt().getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}