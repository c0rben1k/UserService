package by.algin.userservice.service;


import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.entity.User;
import by.algin.userservice.exception.InvalidTokenException;
import by.algin.userservice.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String ACCESS_TOKEN_TYPE = "access";



    @Value("${app.security.secret}")
    private String secretKey;

    @Value("${app.security.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${app.security.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(Authentication authentication) {
        return generateAccessToken(extractUsername(authentication));
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateRefreshToken(extractUsername(authentication));
    }

    public String generateAccessToken(User user) {
        return generateAccessToken(extractUsername(user));
    }

    public String generateRefreshToken(User user) {
        return generateRefreshToken(extractUsername(user));
    }

    private String extractUsername(Authentication authentication) {
        return authentication.getName();
    }

    private String extractUsername(User user) {
        return user.getUsername();
    }

    private String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);
        return generateToken(claims, username, accessTokenExpiration);
    }

    private String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE);
        return generateToken(claims, username, refreshTokenExpiration);
    }

    private String generateToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(MessageConstants.JWT_TOKEN_EXPIRED, e.getMessage());
            throw new TokenExpiredException(MessageConstants.TOKEN_HAS_EXPIRED);
        } catch (SignatureException e) {
            log.error(MessageConstants.INVALID_JWT_SIGNATURE, e.getMessage());
            throw new InvalidTokenException(MessageConstants.INVALID_TOKEN_SIGNATURE);
        } catch (MalformedJwtException e) {
            log.error(MessageConstants.INVALID_JWT_FORMAT, e.getMessage());
            throw new InvalidTokenException(MessageConstants.INVALID_TOKEN_FORMAT);
        } catch (UnsupportedJwtException e) {
            log.error(MessageConstants.UNSUPPORTED_JWT_TOKEN, e.getMessage());
            throw new InvalidTokenException(MessageConstants.UNSUPPORTED_TOKEN_TYPE);
        } catch (Exception e) {
            log.error(MessageConstants.JWT_VALIDATION_FAILED, e.getMessage());
            throw new InvalidTokenException(MessageConstants.TOKEN_VALIDATION_FAILED);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return validateTokenWithClaims(claims, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateTokenWithClaims(Claims claims, UserDetails userDetails) {
        final String username = getUsernameFromClaims(claims);
        final Date expiration = getExpirationFromClaims(claims);

        return (username.equals(userDetails.getUsername()))
            && !expiration.before(new Date())
            && userDetails.isEnabled();
    }



    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getUsernameFromClaims(Claims claims) {
        return claims.getSubject();
    }

    public Date getExpirationFromClaims(Claims claims) {
        return claims.getExpiration();
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Boolean isRefreshToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return isRefreshToken(claims);
        } catch (Exception e) {
            log.error(MessageConstants.FAILED_TO_CHECK_TOKEN_TYPE, e.getMessage());
            return false;
        }
    }

    public Claims validateAndGetClaims(String token) {
        try {
            return getAllClaimsFromToken(token);
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return null;
        }
    }

    public Boolean isRefreshToken(Claims claims) {
        try {
            return REFRESH_TOKEN_TYPE.equals(claims.get(TOKEN_TYPE_CLAIM, String.class));
        } catch (Exception e) {
            log.error(MessageConstants.FAILED_TO_CHECK_TOKEN_TYPE, e.getMessage());
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

}