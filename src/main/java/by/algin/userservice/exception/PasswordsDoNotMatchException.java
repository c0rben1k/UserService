package by.algin.userservice.exception;

public class PasswordsDoNotMatchException extends ApiException {
    public PasswordsDoNotMatchException() {
        super(ErrorCode.PASSWORDS_DONT_MATCH);
    }
}
