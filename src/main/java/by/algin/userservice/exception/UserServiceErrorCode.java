package by.algin.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserServiceErrorCode {

    USER_NOT_FOUND("USER_001", "User not found", HttpStatus.NOT_FOUND, "USER"),
    EMAIL_ALREADY_EXISTS("USER_002", "Email already exists", HttpStatus.CONFLICT, "USER"),
    USERNAME_ALREADY_EXISTS("USER_003", "Username already exists", HttpStatus.CONFLICT, "USER"),
    ROLE_NOT_FOUND("USER_004", "Role not found", HttpStatus.NOT_FOUND, "USER"),
    PASSWORDS_DONT_MATCH("USER_005", "Passwords don't match", HttpStatus.BAD_REQUEST, "USER"),
    INVALID_EMAIL_FORMAT("USER_006", "Invalid email format", HttpStatus.BAD_REQUEST, "USER"),
    ACCOUNT_ALREADY_CONFIRMED("USER_007", "Account is already confirmed", HttpStatus.CONFLICT, "USER"),
    ACCOUNT_DISABLED("USER_008", "Account is disabled", HttpStatus.FORBIDDEN, "USER"),

    INVALID_CREDENTIALS("USER_AUTH_001", "Invalid credentials", HttpStatus.UNAUTHORIZED, "AUTHENTICATION"),
    AUTHENTICATION_FAILED("USER_AUTH_002", "Authentication failed", HttpStatus.UNAUTHORIZED, "AUTHENTICATION"),
    ACCESS_DENIED("USER_AUTH_003", "Access denied", HttpStatus.FORBIDDEN, "AUTHENTICATION"),
    TOKEN_EXPIRED("USER_AUTH_004", "Token has expired", HttpStatus.UNAUTHORIZED, "AUTHENTICATION"),
    INVALID_TOKEN("USER_AUTH_005", "Invalid token", HttpStatus.UNAUTHORIZED, "AUTHENTICATION"),

    RATE_LIMIT_EXCEEDED("USER_RATE_001", "Rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMITING"),

    EMAIL_SENDING_ERROR("USER_EMAIL_001", "Error sending email", HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL");

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;
    private final String category;
}