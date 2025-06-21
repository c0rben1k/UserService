package by.algin.userservice.exception;

import by.algin.common.BaseExceptionHandler;
import by.algin.dto.response.ApiResponse;
import by.algin.userservice.constants.MessageConstants;
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

    @Override
    protected String getServiceName() {
        return MessageConstants.SERVICE_NAME;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_BAD_CREDENTIALS);
        UserServiceErrorCode errorCode = UserServiceErrorCode.INVALID_CREDENTIALS;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthentication(AuthenticationException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_AUTHENTICATION_FAILED);
        details.put(MessageConstants.DETAIL_KEY_EXCEPTION_TYPE, ex.getClass().getSimpleName());
        UserServiceErrorCode errorCode = UserServiceErrorCode.AUTHENTICATION_FAILED;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_ACCESS_DENIED);
        UserServiceErrorCode errorCode = UserServiceErrorCode.ACCESS_DENIED;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenExpired(TokenExpiredException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_TOKEN_EXPIRED);
        UserServiceErrorCode errorCode = UserServiceErrorCode.TOKEN_EXPIRED;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_USER_NOT_FOUND);
        details.put(MessageConstants.DETAIL_KEY_ORIGINAL_MESSAGE, ex.getMessage());
        UserServiceErrorCode errorCode = UserServiceErrorCode.USER_NOT_FOUND;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidEmail(InvalidEmailException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_INVALID_EMAIL_FORMAT);
        details.put(MessageConstants.DETAIL_KEY_ORIGINAL_MESSAGE, ex.getMessage());
        UserServiceErrorCode errorCode = UserServiceErrorCode.INVALID_EMAIL_FORMAT;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }

    @ExceptionHandler(AccountAlreadyConfirmedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountAlreadyConfirmed(AccountAlreadyConfirmedException ex) {
        Map<String, Object> details = new HashMap<>();
        details.put(MessageConstants.DETAIL_KEY_REASON, MessageConstants.REASON_ACCOUNT_ALREADY_CONFIRMED);
        details.put(MessageConstants.DETAIL_KEY_ORIGINAL_MESSAGE, ex.getMessage());
        UserServiceErrorCode errorCode = UserServiceErrorCode.ACCOUNT_ALREADY_CONFIRMED;
        return createError(errorCode.getCode(), errorCode.getDefaultMessage(), errorCode.getHttpStatus().value(), details);
    }
}