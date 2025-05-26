package by.algin.userservice.service;

import by.algin.userservice.config.AppProperties;
import by.algin.userservice.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final AppProperties appProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public boolean isTokenValid(LocalDateTime tokenCreationTime) {
        if (tokenCreationTime == null) {
            return false;
        }

        int expirationMinutes = appProperties.getConfirmation().getExpirationMinutes();

        LocalDateTime expirationTime = tokenCreationTime.plusMinutes(expirationMinutes);
        return LocalDateTime.now().isBefore(expirationTime);
    }

    public void validateToken(LocalDateTime tokenCreationTime, String email) {
        if (!isTokenValid(tokenCreationTime)) {
            log.warn("Token expired for user with email: {}", email);
            throw new TokenExpiredException(email);
        }
    }
}