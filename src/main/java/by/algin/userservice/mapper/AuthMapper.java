package by.algin.userservice.mapper;

import by.algin.userservice.dto.response.AuthResponse;
import by.algin.userservice.dto.response.TokenValidationResponse;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class AuthMapper {

    public AuthResponse toAuthResponse(User user, String accessToken, String refreshToken, Long expiresIn) {
        if (user == null) {
            return null;
        }

        HashSet<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(HashSet::new));

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
        if (!isValid) {
            return TokenValidationResponse.builder()
                    .isValid(false)
                    .build();
        }

        if (user == null) {
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