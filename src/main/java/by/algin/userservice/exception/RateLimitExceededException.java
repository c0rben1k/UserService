package by.algin.userservice.exception;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException() {
        super(UserServiceErrorCode.RATE_LIMIT_EXCEEDED.getDefaultMessage());
    }

    public RateLimitExceededException(String message) {
        super(message);
    }
}
