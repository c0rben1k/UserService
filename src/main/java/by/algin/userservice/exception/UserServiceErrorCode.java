package by.algin.userservice.exception;

import by.algin.common.exception.CommonErrorCodes;
import by.algin.common.exception.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserServiceErrorCode implements ApiErrorCode {

    ROLE_NOT_FOUND("USER_004", "Role not found", HttpStatus.NOT_FOUND, "USER"),
    ACCOUNT_ALREADY_CONFIRMED("USER_007", "Account is already confirmed", HttpStatus.CONFLICT, "USER"),
    EMAIL_SENDING_ERROR("USER_EMAIL_001", "Error sending email", HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL"),

    USER_NOT_FOUND(CommonErrorCodes.USER_NOT_FOUND.getCode(), CommonErrorCodes.USER_NOT_FOUND.getDefaultMessage(), CommonErrorCodes.USER_NOT_FOUND.getHttpStatus(), "USER"),
    EMAIL_ALREADY_EXISTS(CommonErrorCodes.EMAIL_ALREADY_EXISTS.getCode(), CommonErrorCodes.EMAIL_ALREADY_EXISTS.getDefaultMessage(), CommonErrorCodes.EMAIL_ALREADY_EXISTS.getHttpStatus(), "USER"),
    USERNAME_ALREADY_EXISTS(CommonErrorCodes.USERNAME_ALREADY_EXISTS.getCode(), CommonErrorCodes.USERNAME_ALREADY_EXISTS.getDefaultMessage(), CommonErrorCodes.USERNAME_ALREADY_EXISTS.getHttpStatus(), "USER"),
    PASSWORDS_DONT_MATCH(CommonErrorCodes.PASSWORDS_DONT_MATCH.getCode(), CommonErrorCodes.PASSWORDS_DONT_MATCH.getDefaultMessage(), CommonErrorCodes.PASSWORDS_DONT_MATCH.getHttpStatus(), "USER"),
    INVALID_EMAIL_FORMAT(CommonErrorCodes.INVALID_EMAIL_FORMAT.getCode(), CommonErrorCodes.INVALID_EMAIL_FORMAT.getDefaultMessage(), CommonErrorCodes.INVALID_EMAIL_FORMAT.getHttpStatus(), "USER"),
    ACCOUNT_DISABLED(CommonErrorCodes.ACCOUNT_DISABLED.getCode(), CommonErrorCodes.ACCOUNT_DISABLED.getDefaultMessage(), CommonErrorCodes.ACCOUNT_DISABLED.getHttpStatus(), "USER"),
    INVALID_CREDENTIALS(CommonErrorCodes.INVALID_CREDENTIALS.getCode(), CommonErrorCodes.INVALID_CREDENTIALS.getDefaultMessage(), CommonErrorCodes.INVALID_CREDENTIALS.getHttpStatus(), "AUTHENTICATION"),
    AUTHENTICATION_FAILED(CommonErrorCodes.AUTHENTICATION_FAILED.getCode(), CommonErrorCodes.AUTHENTICATION_FAILED.getDefaultMessage(), CommonErrorCodes.AUTHENTICATION_FAILED.getHttpStatus(), "AUTHENTICATION"),
    ACCESS_DENIED(CommonErrorCodes.ACCESS_DENIED.getCode(), CommonErrorCodes.ACCESS_DENIED.getDefaultMessage(), CommonErrorCodes.ACCESS_DENIED.getHttpStatus(), "AUTHENTICATION"),
    TOKEN_EXPIRED(CommonErrorCodes.TOKEN_EXPIRED.getCode(), CommonErrorCodes.TOKEN_EXPIRED.getDefaultMessage(), CommonErrorCodes.TOKEN_EXPIRED.getHttpStatus(), "AUTHENTICATION"),
    INVALID_TOKEN(CommonErrorCodes.INVALID_TOKEN.getCode(), CommonErrorCodes.INVALID_TOKEN.getDefaultMessage(), CommonErrorCodes.INVALID_TOKEN.getHttpStatus(), "AUTHENTICATION"),
    RATE_LIMIT_EXCEEDED(CommonErrorCodes.RATE_LIMIT_EXCEEDED.getCode(), CommonErrorCodes.RATE_LIMIT_EXCEEDED.getDefaultMessage(), CommonErrorCodes.RATE_LIMIT_EXCEEDED.getHttpStatus(), "RATE_LIMITING");

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;
    private final String category;
}