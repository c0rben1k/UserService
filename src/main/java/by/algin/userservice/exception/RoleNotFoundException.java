package by.algin.userservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super(UserServiceErrorCode.ROLE_NOT_FOUND.getDefaultMessage());
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
