package by.algin.userservice.constants;

public final class PathConstants {
    // Web paths
    public static final String ROOT = "/";
    public static final String DASHBOARD = "/dashboard";
    public static final String ADMIN_DASHBOARD = "/admin-dashboard";
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String AUTH_REGISTRATION_SUCCESS = "/auth/registration-success";
    public static final String AUTH_CONFIRM = "/auth/confirm";
    public static final String AUTH_TOKEN_EXPIRED = "/auth/token-expired";
    public static final String AUTH_RESEND_CONFIRMATION = "/auth/resend-confirmation";
    public static final String CSS = "/css/**";
    public static final String JS = "/js/**";
    public static final String IMAGES = "/images/**";

    // API paths
    public static final String API = "/api/**";
    public static final String API_AUTH = "/api/auth/**";
    public static final String API_AUTH_BASE = "/api/auth";
    public static final String API_AUTH_REGISTER = "/api/auth/register";
    public static final String API_AUTH_LOGIN = "/api/auth/login";
    public static final String API_AUTH_REFRESH_TOKEN = "/api/auth/refresh-token";
    public static final String API_AUTH_VALIDATE_TOKEN = "/api/auth/validate-token";
    public static final String API_AUTH_CONFIRM = "/api/auth/confirm";
    public static final String API_AUTH_RESEND_CONFIRMATION = "/api/auth/resend-confirmation";
    public static final String API_USERS = "/api/users";
    public static final String API_USERS_ID = "/api/users/{id}";
    public static final String API_USERS_BY_USERNAME = "/api/users/by-username/{username}";
    public static final String API_USERS_BY_EMAIL = "/api/users/by-email/{email}";
    public static final String API_USERS_SEARCH = "/api/users/search";

    // Template names
    public static final String TEMPLATE_INDEX = "index";
    public static final String TEMPLATE_DASHBOARD = "dashboard";
    public static final String TEMPLATE_ADMIN_DASHBOARD = "admin-dashboard";
    public static final String TEMPLATE_REGISTER = "register";
    public static final String TEMPLATE_LOGIN = "login";
    public static final String TEMPLATE_REGISTRATION_SUCCESS = "registration-success";
    public static final String TEMPLATE_ACCOUNT_CONFIRMED = "account-confirmed";
    public static final String TEMPLATE_TOKEN_EXPIRED = "token-expired";
    public static final String TEMPLATE_TOKEN_RESENT = "token-resent";
    public static final String TEMPLATE_ERROR = "error";
}