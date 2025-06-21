package by.algin.userservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super(UserServiceErrorCode.INVALID_CREDENTIALS.getDefaultMessage());
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
