package by.algin.userservice.constants;

public class PathConstants {

    public static final String API = "/api/**";
    public static final String API_AUTH = "/api/auth/**";
    public static final String API_AUTH_BASE = "/api/auth";
    public static final String API_USERS = "/api/users";
    public static final String API_USERS_SEARCH = "/api/users/search";

    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String VALIDATE = "/validate";

    public static final String REGISTER_ENDPOINT = "/register";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String API_AUTH_VALIDATE_TOKEN = "/validate-token";
    public static final String CONFIRM_ENDPOINT = "/confirm";
    public static final String RESEND_CONFIRMATION_ENDPOINT = "/resend-confirmation";
    public static final String EMAIL_BY_TOKEN_ENDPOINT = "/email-by-token";

    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FIELD = "field";
    public static final String PARAM_VALUE = "value";

    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String AUTH_REGISTRATION_SUCCESS = "/auth/registration-success";
    public static final String AUTH_CONFIRM = "/auth/confirm";
    public static final String AUTH_TOKEN_EXPIRED = "/auth/token-expired";
    public static final String AUTH_RESEND_CONFIRMATION = "/auth/resend-confirmation";
    public static final String AUTH_LOGOUT = "/auth/logout";

    public static final String SEARCH = "/search";
    public static final String USER_BY_ID = "/{userId}";
    public static final String USER_EXISTS = "/{userId}/exists";
    public static final String BATCH_USERS = "/batch";

    public static final String CSS = "/css/**";
    public static final String JS = "/js/**";
    public static final String IMAGES = "/images/**";
    public static final String ROOT = "/";

    public static final String DASHBOARD = "/dashboard";
    public static final String ADMIN_DASHBOARD = "/admin/dashboard";

    public static final String TEMPLATE_LOGIN = "auth/login";
    public static final String TEMPLATE_REGISTER = "auth/register";
    public static final String TEMPLATE_REGISTRATION_SUCCESS = "auth/registration-success";
    public static final String TEMPLATE_ACCOUNT_CONFIRMED = "auth/account-confirmed";
    public static final String TEMPLATE_TOKEN_EXPIRED = "auth/token-expired";
    public static final String TEMPLATE_TOKEN_RESENT = "auth/token-resent";
    public static final String TEMPLATE_INDEX = "index";
    public static final String TEMPLATE_DASHBOARD = "dashboard/dashboard";
    public static final String TEMPLATE_ADMIN_DASHBOARD = "admin/dashboard";
}