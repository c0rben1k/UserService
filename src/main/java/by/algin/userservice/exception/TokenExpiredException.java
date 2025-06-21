package by.algin.userservice.exception;

import by.algin.userservice.constants.MessageConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super(MessageConstants.TOKEN_HAS_EXPIRED);
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}