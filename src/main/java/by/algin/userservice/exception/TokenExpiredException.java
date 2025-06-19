package by.algin.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends ApiException {
    public TokenExpiredException(String message) {
        super(ErrorCode.EXPIRED_TOKEN, message);
    }
}