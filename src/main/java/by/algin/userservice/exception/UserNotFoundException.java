package by.algin.userservice.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super(UserServiceErrorCode.USER_NOT_FOUND.getDefaultMessage());
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}