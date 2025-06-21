package by.algin.userservice.exception;

import by.algin.userservice.constants.MessageConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountDisabledException extends RuntimeException {
    public AccountDisabledException() {
        super(MessageConstants.USER_IS_DISABLED);
    }

    public AccountDisabledException(String details) {
        super(details);
    }

    public AccountDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
