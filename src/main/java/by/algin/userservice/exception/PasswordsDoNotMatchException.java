package by.algin.userservice.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super(UserServiceErrorCode.PASSWORDS_DONT_MATCH.getDefaultMessage());
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
