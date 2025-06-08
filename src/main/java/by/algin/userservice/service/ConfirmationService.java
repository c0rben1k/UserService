package by.algin.userservice.service;

import by.algin.userservice.entity.User;
import by.algin.userservice.repository.UserRepository;
import by.algin.userservice.exception.InvalidEmailException;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.InvalidTokenException;
import by.algin.userservice.exception.AccountAlreadyConfirmedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Async
    @Transactional
    public void sendConfirmationEmail(User user) {
        if (user == null || !StringUtils.hasText(user.getEmail())) {
            log.error("Cannot send confirmation email: user or email is null");
            throw new InvalidEmailException("User or email cannot be null");
        }
        log.info("Confirmation token for {}: {}", user.getEmail(), user.getConfirmationToken());
        // emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationToken()); временно
         log.info("Confirmation email sent to: {}", user.getEmail());
    }

    @Transactional
    public void resendConfirmationToken(String email) {
        if (!StringUtils.hasText(email)) {
            log.error("Cannot resend confirmation token: email is null or empty");
            throw new InvalidEmailException("Email cannot be null or empty");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (user.isEnabled()) {
            throw new AccountAlreadyConfirmedException();
        }

        user.setConfirmationToken(tokenService.generateToken());
        user.setTokenCreationTime(LocalDateTime.now());
        userRepository.save(user);

        sendConfirmationEmail(user);
        log.info("Confirmation token resent for email: {}", email);
    }

    @Transactional
    public void confirmAccount(String token) {
        if (!StringUtils.hasText(token)) {
            log.error("Cannot confirm account: token is null or empty");
            throw new InvalidTokenException();
        }
        User user = userRepository.findByConfirmationToken(token)
                .orElseThrow(InvalidTokenException::new);

        if (!StringUtils.hasText(user.getEmail())) {
            log.error("Cannot confirm account: user email is null for token {}", token);
            throw new InvalidEmailException("User email cannot be null");
        }
        tokenService.validateToken(user.getTokenCreationTime(), user.getEmail());

        user.setEnabled(true);
        user.setConfirmationToken(null);
        user.setTokenCreationTime(null);
        userRepository.save(user);

        log.info("Account confirmed successfully for user: {}", user.getUsername());
    }
}