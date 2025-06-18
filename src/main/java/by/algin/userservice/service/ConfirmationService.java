package by.algin.userservice.service;

import by.algin.userservice.constants.MessageConstants;
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
            log.error(MessageConstants.CANNOT_SEND_EMAIL_USER_NULL);
            throw new InvalidEmailException(MessageConstants.USER_OR_EMAIL_NULL);
        }
        log.info(MessageConstants.CONFIRMATION_TOKEN_FOR_EMAIL, user.getEmail(), user.getConfirmationToken());
        // emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationToken()); временно
         log.info(MessageConstants.CONFIRMATION_EMAIL_SENT_TO_LOG, user.getEmail());
    }

    @Transactional
    public void resendConfirmationToken(String email) {
        if (!StringUtils.hasText(email)) {
            log.error(MessageConstants.CANNOT_RESEND_TOKEN_EMAIL_NULL);
            throw new InvalidEmailException(MessageConstants.EMAIL_NULL_OR_EMPTY);
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
        log.info(MessageConstants.CONFIRMATION_TOKEN_RESENT, email);
    }

    @Transactional
    public void confirmAccount(String token) {
        if (!StringUtils.hasText(token)) {
            log.error(MessageConstants.CANNOT_CONFIRM_TOKEN_NULL);
            throw new InvalidTokenException(MessageConstants.TOKEN_NULL_OR_EMPTY);
        }
        User user = userRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new InvalidTokenException(MessageConstants.INVALID_CONFIRMATION_TOKEN));

        if (!StringUtils.hasText(user.getEmail())) {
            log.error(MessageConstants.CANNOT_CONFIRM_EMAIL_NULL, token);
            throw new InvalidEmailException(MessageConstants.USER_EMAIL_NULL);
        }
        tokenService.validateToken(user.getTokenCreationTime(), user.getEmail());

        user.setEnabled(true);
        user.setConfirmationToken(null);
        user.setTokenCreationTime(null);
        userRepository.save(user);

        log.info(MessageConstants.ACCOUNT_CONFIRMED_FOR_USER, user.getUsername());
    }
}