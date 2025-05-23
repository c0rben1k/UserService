package by.algin.userservice.exception;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }
}
