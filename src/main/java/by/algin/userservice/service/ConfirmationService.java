package by.algin.userservice.service;

import by.algin.userservice.entity.User;
import by.algin.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.InvalidTokenException;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Transactional
    public void sendConfirmationEmail(User user) {
        emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationToken());
        log.info("Confirmation email sent to: {}", user.getEmail());
    }

    @Transactional
    public void resendConfirmationToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (user.isEnabled()) {
            throw new IllegalStateException("Account is already confirmed");
        }

        user.setConfirmationToken(tokenService.generateToken());
        user.setTokenCreationTime(LocalDateTime.now());
        userRepository.save(user);

        sendConfirmationEmail(user);
        log.info("Confirmation token resent for email: {}", email);
    }

    @Transactional
    public void confirmAccount(String token) {
        User user = userRepository.findByConfirmationToken(token)
                .orElseThrow(InvalidTokenException::new);

        tokenService.validateToken(user.getTokenCreationTime(), user.getEmail());

        user.setEnabled(true);
        user.setConfirmationToken(null);
        user.setTokenCreationTime(null);
        userRepository.save(user);

        log.info("Account confirmed successfully for user: {}", user.getUsername());
    }
}