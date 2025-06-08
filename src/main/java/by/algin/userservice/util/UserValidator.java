package by.algin.userservice.util;

import by.algin.userservice.dto.request.RegisterRequest;
import by.algin.userservice.exception.EmailAlreadyExistsException;
import by.algin.userservice.exception.PasswordsDoNotMatchException;
import by.algin.userservice.exception.UsernameAlreadyExistsException;
import by.algin.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateRegistrationRequest(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
    }
}