package by.algin.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password"),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "Account is disabled"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token has expired"),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "Token is required"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username already exists"),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Role not found"),
    PASSWORDS_DONT_MATCH(HttpStatus.BAD_REQUEST, "Passwords don't match"),


    EMAIL_SENDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending email"),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "Constraint violation"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

    private final HttpStatus status;
    private final String message;
}