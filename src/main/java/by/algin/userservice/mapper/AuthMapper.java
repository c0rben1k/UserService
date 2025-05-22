package by.algin.userservice.mapper;

import by.algin.userservice.DTO.response.AuthResponse;
import by.algin.userservice.DTO.response.TokenValidationResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthMapper {

    public AuthResponse toAuthResponse(User user, String accessToken, String refreshToken, Long expiresIn) {
        if (user == null) {
            return null;
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    public TokenValidationResponse toTokenValidationResponse(User user, Claims claims, boolean isValid) {
        if (!isValid) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
        }

        if (user == null) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Map<String, Object> customClaims = new HashMap<>();
        if (claims != null) {
            customClaims.putAll(claims);
            customClaims.remove("sub");
            customClaims.remove("iat");
            customClaims.remove("exp");
            customClaims.remove("authorities");
        }

        return TokenValidationResponse.builder()
                .valid(true)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .claims(customClaims)
                .build();
    }

    public TokenValidationResponse toInvalidTokenResponse(String message) {
        return TokenValidationResponse.builder()
                .valid(false)
                .build();
    }
}