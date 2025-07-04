package by.algin.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super(UserServiceErrorCode.TOKEN_EXPIRED.getDefaultMessage());
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}