package by.algin.userservice.exception;

public class TokenExpiredException extends RuntimeException {
    private final String email;

    public TokenExpiredException(String email) {
        super(ErrorCode.EXPIRED_TOKEN.getMessage());
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}