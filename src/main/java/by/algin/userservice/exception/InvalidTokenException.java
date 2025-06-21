package by.algin.userservice.exception;

import by.algin.userservice.constants.MessageConstants;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(MessageConstants.INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
