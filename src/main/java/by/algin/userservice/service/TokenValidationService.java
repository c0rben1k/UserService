package by.algin.userservice.service;

import by.algin.dto.request.TokenValidationRequest;
import by.algin.dto.response.ApiResponse;
import by.algin.dto.response.TokenValidationResponse;
import by.algin.userservice.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidationService {
    
    private final AuthService authService;

    public ApiResponse<TokenValidationResponse> validateTokenFromHeader(String authHeader) {
        String token = extractTokenFromAuthHeader(authHeader);
        
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken(token);
        
        log.debug("Processing token validation request");
        return authService.validateToken(request);
    }

    public boolean validateTokenSimple(String authHeader) {
        try {
            ApiResponse<TokenValidationResponse> response = validateTokenFromHeader(authHeader);
            return response.isSuccess() && response.getData() != null && response.getData().isValid();
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private String extractTokenFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Invalid or missing Authorization header");
            throw new InvalidTokenException("Missing or invalid Authorization header format. Expected: Bearer <token>");
        }
        
        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            log.debug("Empty token in Authorization header");
            throw new InvalidTokenException("Token is empty");
        }
        
        return token;
    }
}
