package by.algin.userservice.exception;

import by.algin.common.BaseExceptionHandler;
import by.algin.dto.response.ApiResponse;
import by.algin.userservice.constants.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceGlobalExceptionHandler extends BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceGlobalExceptionHandler.class);

    @Override
    protected String getServiceName() {
        return MessageConstants.SERVICE_NAME;
    }

    private ResponseEntity<ApiResponse<Object>> handleException(
            UserServiceErrorCode errorCode,
            String reasonKey,
            Exception ex,
            boolean includeOriginalMessage) {
        return handleException(errorCode, reasonKey, ex, includeOriginalMessage, null);
    }

    private ResponseEntity<ApiResponse<Object>> handleException(
            UserServiceErrorCode errorCode,
            String reasonKey,
            Exception ex,
            boolean includeOriginalMessage,
            Map<String, Object> additionalDetails) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, reasonKey);

        if (includeOriginalMessage && ex.getMessage() != null) {
            details.put(MessageConstants.DETAIL_KEY_ORIGINAL_MESSAGE, ex.getMessage());
        }

        if (ex instanceof AuthenticationException) {
            details.put(MessageConstants.DETAIL_KEY_EXCEPTION_TYPE, ex.getClass().getSimpleName());
        }

        if (additionalDetails != null) {
            details.putAll(additionalDetails);
        }

        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    private ResponseEntity<ApiResponse<Object>> handleUserException(UserServiceErrorCode errorCode, String reasonKey, Exception ex) {
        logger.warn("{}: {}", errorCode.getDefaultMessage(), ex.getMessage());
        return handleException(errorCode, reasonKey, ex, true);
    }

    private ResponseEntity<ApiResponse<Object>> handleAuthException(UserServiceErrorCode errorCode, String reasonKey, Exception ex) {
        logger.warn("Authentication error - {}: {}", errorCode.getDefaultMessage(), ex.getMessage());
        return handleException(errorCode, reasonKey, ex, false);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return handleAuthException(UserServiceErrorCode.INVALID_CREDENTIALS, MessageConstants.REASON_BAD_CREDENTIALS, ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthentication(AuthenticationException ex) {
        return handleAuthException(UserServiceErrorCode.AUTHENTICATION_FAILED, MessageConstants.REASON_AUTHENTICATION_FAILED, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return handleAuthException(UserServiceErrorCode.ACCESS_DENIED, MessageConstants.REASON_ACCESS_DENIED, ex);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenExpired(TokenExpiredException ex) {
        return handleAuthException(UserServiceErrorCode.TOKEN_EXPIRED, MessageConstants.REASON_TOKEN_EXPIRED, ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        return handleUserException(UserServiceErrorCode.USER_NOT_FOUND, MessageConstants.REASON_USER_NOT_FOUND, ex);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidEmail(InvalidEmailException ex) {
        return handleUserException(UserServiceErrorCode.INVALID_EMAIL_FORMAT, MessageConstants.REASON_INVALID_EMAIL_FORMAT, ex);
    }

    @ExceptionHandler(AccountAlreadyConfirmedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountAlreadyConfirmed(AccountAlreadyConfirmedException ex) {
        return handleUserException(UserServiceErrorCode.ACCOUNT_ALREADY_CONFIRMED, MessageConstants.REASON_ACCOUNT_ALREADY_CONFIRMED, ex);
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountDisabled(AccountDisabledException ex) {
        return handleUserException(UserServiceErrorCode.ACCOUNT_DISABLED, "account_disabled", ex);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return handleUserException(UserServiceErrorCode.EMAIL_ALREADY_EXISTS, "email_already_exists", ex);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        return handleUserException(UserServiceErrorCode.USERNAME_ALREADY_EXISTS, "username_already_exists", ex);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<ApiResponse<Object>> handlePasswordsDoNotMatch(PasswordsDoNotMatchException ex) {
        return handleUserException(UserServiceErrorCode.PASSWORDS_DONT_MATCH, "passwords_dont_match", ex);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidToken(InvalidTokenException ex) {
        return handleAuthException(UserServiceErrorCode.INVALID_TOKEN, "invalid_token", ex);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleRoleNotFound(RoleNotFoundException ex) {
        logger.error("Role not found: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.ROLE_NOT_FOUND, "role_not_found", ex, true);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        return handleUserException(UserServiceErrorCode.RATE_LIMIT_EXCEEDED, "rate_limit_exceeded", ex);
    }
}