package by.algin.userservice.mapper;

import by.algin.dto.response.AuthResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

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
            return TokenValidationResponse.builder()
                    .isValid(false)
                    .build();
        }

        HashSet<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(HashSet::new));

        HashMap<String, Object> customClaims = new HashMap<>();
        if (claims != null) {
            customClaims.putAll(claims);
            customClaims.remove("sub");
            customClaims.remove("iat");
            customClaims.remove("exp");
            customClaims.remove("authorities");
        }

        return TokenValidationResponse.builder()
                .isValid(true)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .claims(customClaims)
                .build();
    }

    public TokenValidationResponse toInvalidTokenResponse(String message) {
        return TokenValidationResponse.builder()
                .isValid(false)
                .build();
    }
}