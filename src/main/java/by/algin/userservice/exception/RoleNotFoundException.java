package by.algin.userservice.exception;

public class RoleNotFoundException extends ApiException {
    public RoleNotFoundException() {
        super(ErrorCode.ROLE_NOT_FOUND);
    }
}
