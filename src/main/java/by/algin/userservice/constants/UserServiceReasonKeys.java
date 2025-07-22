package by.algin.userservice.constants;

public class UserServiceReasonKeys {

    public static final String REASON_BAD_CREDENTIALS = "bad_credentials";
    public static final String REASON_AUTHENTICATION_FAILED = "authentication_failed";
    public static final String REASON_ACCESS_DENIED = "access_denied";
    public static final String REASON_TOKEN_EXPIRED = "token_expired";
    public static final String REASON_INVALID_TOKEN = "invalid_token";

    public static final String REASON_USER_NOT_FOUND = "user_not_found";
    public static final String REASON_ALREADY_EXISTS = "already_exists";
    public static final String REASON_VALIDATION_FAILED = "validation_failed";
    public static final String REASON_INVALID_EMAIL_FORMAT = "invalid_email_format";
    public static final String REASON_ACCOUNT_ALREADY_CONFIRMED = "account_already_confirmed";
    public static final String REASON_RESOURCE_NOT_FOUND = "resource_not_found";
    public static final String REASON_RATE_LIMIT_EXCEEDED = "rate_limit_exceeded";
    
    private UserServiceReasonKeys() {}
}
