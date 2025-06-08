package by.algin.userservice.exception;

public class MissingTokenException extends ApiException {
    public MissingTokenException() {
        super(ErrorCode.MISSING_TOKEN);
    }
}
