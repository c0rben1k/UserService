package by.algin.userservice.exception;



public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super(UserServiceErrorCode.USERNAME_ALREADY_EXISTS.getDefaultMessage());
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
