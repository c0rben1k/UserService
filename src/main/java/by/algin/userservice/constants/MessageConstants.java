package by.algin.userservice.constants;

public class MessageConstants {

    public static final String SERVICE_NAME = "UserService";

    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String LOGIN_SUCCESSFUL_FOR_USER = "Login successful for user: {}";
    public static final String TOKEN_REFRESHED_SUCCESSFULLY = "Token refreshed successfully";
    public static final String TOKEN_REFRESHED_FOR_USER = "Token refreshed for user: {}";
    public static final String TOKEN_VALIDATED_SUCCESSFULLY = "Token validated successfully";
    public static final String TOKEN_VALIDATED_FOR_USER = "Token validated for user: {}";
    public static final String TOKEN_IS_VALID = "Token is valid";
    public static final String ACCOUNT_CONFIRMED_SUCCESSFULLY = "Account confirmed successfully";
    public static final String CONFIRMATION_EMAIL_RESENT = "Confirmation email resent successfully";
    public static final String CONFIRMATION_TOKEN_RESENT = "Confirmation token resent for email: {}";
    public static final String EMAIL_RETRIEVED_SUCCESSFULLY = "Email retrieved successfully";

    public static final String PROCESSING_LOGIN_REQUEST = "Processing login request for: {}";
    public static final String PROCESSING_TOKEN_REFRESH = "Processing token refresh request";
    public static final String PROCESSING_TOKEN_VALIDATION = "Processing token validation request";

    public static final String USER_FOUND = "User found: {}";
    public static final String USER_FOUND_SIMPLE = "User found";
    public static final String USER_ROLES_BEFORE_MAPPING = "User roles before mapping: {}";
    public static final String ROLE_LOG = "Role: {}";
    public static final String AUTH_RESPONSE_ROLES = "Auth response roles: {}";
    public static final String REGISTERING_USER = "Registering user: {}";
    public static final String USER_REGISTERED_WITH_ID = "User registered with ID: {}";
    public static final String CHECKING_RATE_LIMIT = "Checking rate limit for email: {}";
    public static final String GETTING_USER_BY_FIELD = "Getting user by field: {} with value: {}";

    public static final String USER_NOT_FOUND_WITH_ID = "User not found with ID: ";
    public static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: ";
    public static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email: ";
    public static final String INVALID_ID_FORMAT = "Invalid ID format: ";
    public static final String INVALID_SEARCH_FIELD = "Invalid search field: ";
    public static final String VALID_SEARCH_FIELDS = ". Valid fields are: id, username, email";

    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_CREDENTIALS_FOR_USER = "Invalid credentials for user: {}";
    public static final String ACCOUNT_DISABLED_FOR_USER = "Account disabled for user: {}";
    public static final String ERROR_MSG_ACCOUNT_DISABLED_FOR_USER = "Account is disabled for user: ";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_TOKEN_PROVIDED = "Invalid token provided";
    public static final String TOKEN_VALID_USER_DISABLED = "Token is valid but user is disabled: {}";
    public static final String USER_IS_DISABLED = "User is disabled";
    public static final String ACCOUNT_ALREADY_CONFIRMED = "Account is already confirmed";
    public static final String CONFIRMATION_TOKEN_EXPIRED = "Confirmation token has expired";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";

    public static final String REFRESH_TOKEN_NULL_OR_EMPTY = "Refresh token is null or empty";
    public static final String REFRESH_TOKEN_NULL_OR_EMPTY_LOG = "Refresh token is null or empty";
    public static final String INVALID_REFRESH_TOKEN_FORMAT = "Invalid refresh token format";
    public static final String INVALID_REFRESH_TOKEN_FORMAT_LOG = "Invalid refresh token format";
    public static final String TOKEN_NOT_REFRESH_TOKEN = "Token is not a refresh token";
    public static final String TOKEN_NOT_REFRESH_TOKEN_LOG = "Token is not a refresh token";
    public static final String MISSING_TOKEN = "Missing token";

    public static final String JWT_TOKEN_EXPIRED = "JWT token has expired: {}";
    public static final String TOKEN_HAS_EXPIRED = "Token has expired";
    public static final String INVALID_JWT_SIGNATURE = "Invalid JWT signature: {}";
    public static final String INVALID_TOKEN_SIGNATURE = "Invalid token signature";
    public static final String INVALID_JWT_FORMAT = "Invalid JWT format: {}";
    public static final String INVALID_TOKEN_FORMAT = "Invalid token format";
    public static final String UNSUPPORTED_JWT_TOKEN = "Unsupported JWT token: {}";
    public static final String UNSUPPORTED_TOKEN_TYPE = "Unsupported token type";
    public static final String JWT_VALIDATION_FAILED = "JWT validation failed: {}";
    public static final String TOKEN_VALIDATION_FAILED = "Token validation failed";
    public static final String FAILED_TO_CHECK_TOKEN_TYPE = "Failed to check token type: {}";
    public static final String JWT_AUTHENTICATION_FAILED = "JWT authentication failed";

    public static final String CONFIRMATION_EMAIL_SENT_TO = "Confirmation email sent to: {}";
    public static final String CONFIRMATION_EMAIL_SENT_TO_LOG = "Confirmation email sent to: {}";
    public static final String FAILED_TO_SEND_EMAIL_TO = "Failed to send email to: {}";
    public static final String FAILED_TO_SEND_EMAIL = "Failed to send email: ";
    public static final String CANNOT_SEND_EMAIL_USER_NULL = "Cannot send email: user is null";
    public static final String USER_OR_EMAIL_NULL = "User or email is null";
    public static final String CANNOT_RESEND_TOKEN_EMAIL_NULL = "Cannot resend token: email is null";
    public static final String EMAIL_NULL_OR_EMPTY = "Email is null or empty";
    public static final String CONFIRMATION_TOKEN_FOR_EMAIL = "Confirmation token for email {}: {}";
    public static final String ACCOUNT_CONFIRMED_FOR_USER = "Account confirmed for user: {}";

    public static final String CANNOT_CONFIRM_TOKEN_NULL = "Cannot confirm account: token is null";
    public static final String TOKEN_NULL_OR_EMPTY = "Token is null or empty";
    public static final String INVALID_CONFIRMATION_TOKEN = "Invalid confirmation token";
    public static final String CANNOT_CONFIRM_EMAIL_NULL = "Cannot confirm account: user email is null for token: {}";
    public static final String USER_EMAIL_NULL = "User email is null";

    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters long";
    public static final String USERNAME_TOO_SHORT = "Username must be at least 3 characters long";
    public static final String FIELD_REQUIRED = "This field is required";

    public static final String REASON_BAD_CREDENTIALS = "bad_credentials";
    public static final String REASON_AUTHENTICATION_FAILED = "authentication_failed";
    public static final String REASON_ACCESS_DENIED = "access_denied";
    public static final String REASON_TOKEN_EXPIRED = "token_expired";
    public static final String REASON_USER_NOT_FOUND = "user_not_found";
    public static final String REASON_INVALID_EMAIL_FORMAT = "invalid_email_format";
    public static final String REASON_ACCOUNT_ALREADY_CONFIRMED = "account_already_confirmed";

    public static final String DETAIL_KEY_REASON = "reason";
    public static final String DETAIL_KEY_EXCEPTION_TYPE = "exceptionType";
}