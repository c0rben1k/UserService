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
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, reasonKey);

        if (includeOriginalMessage && ex.getMessage() != null) {
            details.put(MessageConstants.DETAIL_KEY_ORIGINAL_MESSAGE, ex.getMessage());
        }

        if (ex instanceof AuthenticationException) {
            details.put(MessageConstants.DETAIL_KEY_EXCEPTION_TYPE, ex.getClass().getSimpleName());
        }

        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        logger.warn("Bad credentials attempt: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.INVALID_CREDENTIALS, MessageConstants.REASON_BAD_CREDENTIALS, ex, false);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthentication(AuthenticationException ex) {
        logger.warn("Authentication failed: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return handleException(UserServiceErrorCode.AUTHENTICATION_FAILED, MessageConstants.REASON_AUTHENTICATION_FAILED, ex, false);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.ACCESS_DENIED, MessageConstants.REASON_ACCESS_DENIED, ex, false);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenExpired(TokenExpiredException ex) {
        logger.warn("Token expired: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.TOKEN_EXPIRED, MessageConstants.REASON_TOKEN_EXPIRED, ex, false);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        logger.warn("User not found: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.USER_NOT_FOUND, MessageConstants.REASON_USER_NOT_FOUND, ex, true);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidEmail(InvalidEmailException ex) {
        logger.warn("Invalid email format: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.INVALID_EMAIL_FORMAT, MessageConstants.REASON_INVALID_EMAIL_FORMAT, ex, true);
    }

    @ExceptionHandler(AccountAlreadyConfirmedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountAlreadyConfirmed(AccountAlreadyConfirmedException ex) {
        logger.warn("Account already confirmed: {}", ex.getMessage());
        return handleException(UserServiceErrorCode.ACCOUNT_ALREADY_CONFIRMED, MessageConstants.REASON_ACCOUNT_ALREADY_CONFIRMED, ex, true);
    }
}