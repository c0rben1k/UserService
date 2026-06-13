package by.algin.userservice.exception;

import by.algin.common.BaseExceptionHandler;
import by.algin.common.exception.CommonErrorCodes;
import by.algin.dto.response.ApiResponse;
import by.algin.userservice.constants.MessageConstants;
import by.algin.userservice.constants.UserServiceReasonKeys;
import by.algin.userservice.exception.UserNotFoundException;
import by.algin.userservice.exception.EmailAlreadyExistsException;
import by.algin.userservice.exception.UsernameAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class UserServiceGlobalExceptionHandler extends BaseExceptionHandler {

    @Override
    protected String getServiceName() {
        return MessageConstants.SERVICE_NAME;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials: {}", ex.getMessage());
        return createMappedError(ex,
                               CommonErrorCodes.INVALID_CREDENTIALS.getCode(),
                               "Invalid username or password",
                               CommonErrorCodes.INVALID_CREDENTIALS.getHttpStatus().value());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountDisabled(DisabledException ex) {
        log.warn("Account disabled: {}", ex.getMessage());
        return createMappedError(ex,
                               CommonErrorCodes.ACCOUNT_DISABLED.getCode(),
                               "Account is disabled",
                               CommonErrorCodes.ACCOUNT_DISABLED.getHttpStatus().value());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthentication(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return createMappedError(ex,
                               CommonErrorCodes.AUTHENTICATION_FAILED.getCode(),
                               ex.getMessage(),
                               CommonErrorCodes.AUTHENTICATION_FAILED.getHttpStatus().value());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return createMappedError(ex,
                               CommonErrorCodes.USER_NOT_FOUND.getCode(),
                               ex.getMessage(),
                               CommonErrorCodes.USER_NOT_FOUND.getHttpStatus().value());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        log.warn("Email already exists: {}", ex.getMessage());
        return createMappedError(ex,
                               CommonErrorCodes.EMAIL_ALREADY_EXISTS.getCode(),
                               ex.getMessage(),
                               CommonErrorCodes.EMAIL_ALREADY_EXISTS.getHttpStatus().value());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        log.warn("Username already exists: {}", ex.getMessage());
        return createMappedError(ex,
                               CommonErrorCodes.USERNAME_ALREADY_EXISTS.getCode(),
                               ex.getMessage(),
                               CommonErrorCodes.USERNAME_ALREADY_EXISTS.getHttpStatus().value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed: {}", errors);
        return createMappedError(ex,
                                CommonErrorCodes.VALIDATION_FAILED.getCode(),
                                "Validation failed: " + errors.toString(),
                                CommonErrorCodes.VALIDATION_FAILED.getHttpStatus().value());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return createMappedError(ex,
                                CommonErrorCodes.INVALID_INPUT.getCode(),
                                ex.getMessage(),
                                CommonErrorCodes.INVALID_INPUT.getHttpStatus().value());
    }
}
