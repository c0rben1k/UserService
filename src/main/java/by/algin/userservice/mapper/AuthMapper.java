package by.algin.userservice.mapper;

import by.algin.dto.response.AuthResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

@Slf4j
@Component
public class AuthMapper {

    public AuthResponse toAuthResponse(User user, String accessToken, String refreshToken, Long expiresIn) {
        if (user == null) {
            return null;
        }

        log.info("User roles in mapper: {}", user.getRoles());
        log.info("User roles size: {}", user.getRoles() != null ? user.getRoles().size() : "null");

        HashSet<String> roles = user.getRoles() != null
                ? user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(HashSet::new))
                : new HashSet<>();

        log.info("Mapped roles: {}", roles);

        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .build();
    }

    public TokenValidationResponse toTokenValidationResponse(User user, Claims claims, boolean isValid) {
        if (!isValid || user == null) {
            return TokenValidationResponse.invalid("Token validation failed");
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Map<String, Object> customClaims = extractCustomClaims(claims);

        return TokenValidationResponse.valid(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles,
                customClaims
        );
    }

    public TokenValidationResponse toInvalidTokenResponse(String message) {
        log.warn("Invalid token: {}", message);
        return TokenValidationResponse.invalid(message);
    }

    private Map<String, Object> extractCustomClaims(Claims claims) {
        if (claims == null) {
            return new HashMap<>();
        }

        Map<String, Object> customClaims = new HashMap<>(claims);
        customClaims.keySet().removeAll(Set.of("sub", "iat", "exp", "authorities"));
        return customClaims;
    }
}