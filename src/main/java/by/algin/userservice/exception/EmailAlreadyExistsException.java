package by.algin.userservice.exception;

public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
