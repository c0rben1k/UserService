package by.algin.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import by.algin.userservice.exception.ErrorCode;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountDisabledException extends ApiException {
    public AccountDisabledException() {
        super(ErrorCode.ACCOUNT_DISABLED);
    }

    public AccountDisabledException(String details) {
        super(ErrorCode.ACCOUNT_DISABLED, details);
    }

    public AccountDisabledException(String message, Throwable cause) {
        this(message);
        initCause(cause);
    }
}
