package by.algin.userservice.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(UserServiceErrorCode.INVALID_TOKEN.getDefaultMessage());
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
