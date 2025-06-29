package by.algin.userservice.exception;

import by.algin.userservice.constants.MessageConstants;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException() {
        super(MessageConstants.MISSING_TOKEN);
    }

    public MissingTokenException(String message) {
        super(message);
    }
}
