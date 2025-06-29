package by.algin.userservice.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super(UserServiceErrorCode.EMAIL_ALREADY_EXISTS.getDefaultMessage());
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
