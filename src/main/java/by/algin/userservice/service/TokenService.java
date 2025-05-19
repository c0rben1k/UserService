package by.algin.userservice.service;

import by.algin.userservice.config.AppProperties;
import by.algin.userservice.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final AppProperties appProperties;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isTokenValid(LocalDateTime tokenCreationTime) {
        if (tokenCreationTime == null) {
            return false;
        }

        int expirationMinutes = appProperties.getConfirmation().getToken().getExpirationMinutes();

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