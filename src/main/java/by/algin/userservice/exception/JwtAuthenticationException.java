package by.algin.userservice.exception;

import by.algin.userservice.constants.MessageConstants;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException() {
        super(MessageConstants.JWT_AUTHENTICATION_FAILED);
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
